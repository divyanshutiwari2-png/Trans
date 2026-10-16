package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.Language
import com.example.data.model.SpeakerSide
import com.example.data.model.TravelScenario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GeminiTranslationResponse(
    val translatedText: String,
    val phonetic: String = "",
    val culturalNuance: String = "",
    val suggestedReplies: List<String> = emptyList(),
    val isAiGenerated: Boolean = true
)

object GeminiTranslationService {
    private const val TAG = "GeminiTranslation"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"
    private const val PRIMARY_MODEL = "gemini-3.8-flash"
    private const val FALLBACK_MODEL = "gemini-3.5-flash"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun translateConversation(
        text: String,
        sourceLang: Language,
        targetLang: Language,
        speaker: SpeakerSide,
        scenario: TravelScenario
    ): GeminiTranslationResponse = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (!isApiKeyConfigured()) {
            Log.w(TAG, "Gemini API key is not set or placeholder. Providing contextual travel fallback.")
            return@withContext provideOfflineTravelFallback(text, sourceLang, targetLang, speaker, scenario)
        }

        // Try primary model, fallback if needed
        try {
            return@withContext callGemini(PRIMARY_MODEL, apiKey, text, sourceLang, targetLang, speaker, scenario)
        } catch (e: Exception) {
            Log.e(TAG, "Primary model $PRIMARY_MODEL failed: ${e.message}. Attempting $FALLBACK_MODEL", e)
            try {
                return@withContext callGemini(FALLBACK_MODEL, apiKey, text, sourceLang, targetLang, speaker, scenario)
            } catch (e2: Exception) {
                Log.e(TAG, "Fallback model $FALLBACK_MODEL also failed: ${e2.message}", e2)
                return@withContext provideOfflineTravelFallback(text, sourceLang, targetLang, speaker, scenario)
            }
        }
    }

    private fun callGemini(
        model: String,
        apiKey: String,
        text: String,
        sourceLang: Language,
        targetLang: Language,
        speaker: SpeakerSide,
        scenario: TravelScenario
    ): GeminiTranslationResponse {
        val endpoint = "$BASE_URL$model:generateContent?key=$apiKey"

        val systemPrompt = """
            You are VoxTravel, an expert real-time voice interpreter and cultural travel companion.
            Task: Translate the spoken utterance accurately and naturally from ${sourceLang.displayName} (${sourceLang.code}) into ${targetLang.displayName} (${targetLang.code}).
            Speaker: ${if (speaker == SpeakerSide.TRAVELER) "Visiting Traveler" else "Local Native Speaker"}.
            Travel Context Scenario: ${scenario.title} (${scenario.description}).
            
            Rules:
            1. Provide natural spoken idiom suitable for real-time live travel interaction (use polite forms where customary, such as Japanese keigo/desu-masu, Korean jondaetmal, Spanish usted/tú based on context).
            2. For languages with non-Latin scripts (Japanese, Chinese, Korean, Arabic, Thai, Hindi, Russian, etc.), provide clear romanized phonetic pronunciation (Romaji/Pinyin/Romanization) so the speaker can pronounce it easily.
            3. Provide a concise 1-sentence cultural nuance, etiquette tip, or gesture recommendation for this exact exchange.
            4. Suggest 2-3 short, natural replies the other person might say or that the speaker could follow up with.
            
            Respond strictly in valid JSON format with keys:
            {
              "translatedText": "string",
              "phonetic": "string",
              "culturalNuance": "string",
              "suggestedReplies": ["string", "string"]
            }
        """.trimIndent()

        val requestJson = JSONObject().apply {
            val contentsArray = JSONArray()
            val contentObj = JSONObject().apply {
                val partsArray = JSONArray()
                partsArray.put(JSONObject().apply {
                    put("text", "Utterance to translate: \"$text\"")
                })
                put("parts", partsArray)
            }
            contentsArray.put(contentObj)
            put("contents", contentsArray)

            // System instruction
            put("systemInstruction", JSONObject().apply {
                val sysParts = JSONArray()
                sysParts.put(JSONObject().apply {
                    put("text", systemPrompt)
                })
                put("parts", sysParts)
            })

            // Generation config
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.3)
            })
        }

        val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(endpoint)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw IllegalStateException("Empty Gemini API response")

        if (!response.isSuccessful) {
            throw IllegalStateException("Gemini API error code ${response.code}: $responseBody")
        }

        return parseGeminiJsonResponse(responseBody)
    }

    private fun parseGeminiJsonResponse(rawJson: String): GeminiTranslationResponse {
        val root = JSONObject(rawJson)
        val candidates = root.optJSONArray("candidates")
        if (candidates == null || candidates.length() == 0) {
            throw IllegalStateException("No candidates returned from Gemini")
        }
        val firstCandidate = candidates.getJSONObject(0)
        val content = firstCandidate.getJSONObject("content")
        val parts = content.getJSONArray("parts")
        val textPart = parts.getJSONObject(0).getString("text")

        // Parse inner JSON
        val cleanedText = textPart.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
        val structured = JSONObject(cleanedText)

        val translated = structured.optString("translatedText", "")
        val phonetic = structured.optString("phonetic", "")
        val cultural = structured.optString("culturalNuance", "")
        val repliesArray = structured.optJSONArray("suggestedReplies")
        val replies = mutableListOf<String>()
        if (repliesArray != null) {
            for (i in 0 until repliesArray.length()) {
                val r = repliesArray.optString(i)
                if (r.isNotBlank()) replies.add(r)
            }
        }

        return GeminiTranslationResponse(
            translatedText = translated,
            phonetic = phonetic,
            culturalNuance = cultural,
            suggestedReplies = replies,
            isAiGenerated = true
        )
    }

    private fun provideOfflineTravelFallback(
        text: String,
        sourceLang: Language,
        targetLang: Language,
        speaker: SpeakerSide,
        scenario: TravelScenario
    ): GeminiTranslationResponse {
        val lower = text.lowercase().trim()

        val (translated, phonetic, nuance, replies) = when {
            lower.contains("hello") || lower.contains("hi") -> when (targetLang.code) {
                "ja" -> Quad("こんにちは", "Konnichiwa", "Give a slight respectful bow when greeting in Japan.", listOf("こんにちは！", "初めまして！"))
                "es" -> Quad("¡Hola! ¿Cómo está?", "Oh-la", "A warm greeting is appreciated in Latin cultures.", listOf("¡Hola, muy bien!", "Buenos días"))
                "fr" -> Quad("Bonjour ! Comment allez-vous ?", "Bohn-zhoor", "Always say Bonjour upon entering any shop.", listOf("Bonjour !", "Très bien, merci !"))
                "it" -> Quad("Buongiorno!", "Bwon-jor-no", "Use Buongiorno in day, Buonasera in evening.", listOf("Buongiorno!", "Tutto bene!"))
                "ko" -> Quad("안녕하세요!", "An-nyeong-ha-se-yo", "A small two-handed gesture or bow is polite.", listOf("안녕하세요!", "반갑습니다!"))
                "zh" -> Quad("你好！", "Nǐ hǎo", "Polite and universal greeting across China.", listOf("你好！", "很高兴见到你！"))
                "de" -> Quad("Guten Tag!", "Goo-ten Tahk", "Firm handshake and direct eye contact.", listOf("Guten Tag!", "Hallo!"))
                else -> Quad("Hello / Welcome!", "", "Standard friendly international greeting.", listOf("Hello!", "Nice to meet you!"))
            }

            lower.contains("thank") || lower.contains("gracias") || lower.contains("arigato") -> when (targetLang.code) {
                "ja" -> Quad("ありがとうございます！", "Arigatō gozaimasu!", "Use the full polite form 'gozaimasu' with strangers.", listOf("どういたしまして！", "良い旅を！"))
                "es" -> Quad("¡Muchas gracias!", "Moo-chas grah-syahs", "Gratitude is welcomed with 'De nada' (You're welcome).", listOf("De nada", "Con gusto"))
                "fr" -> Quad("Merci beaucoup !", "Mair-see boh-koo", "Pair with a friendly smile.", listOf("De rien !", "Je vous en prie !"))
                "it" -> Quad("Grazie mille!", "Grah-tsee-eh meel-leh", "Polite reply is 'Prego!'.", listOf("Prego!", "Di niente!"))
                "ko" -> Quad("정말 감사합니다!", "Jeong-mal gam-sa-ham-ni-da!", "Deep bow accompanies sincere thanks in Korea.", listOf("천만에요!", "별말씀을요!"))
                "zh" -> Quad("非常感谢！", "Fēicháng gǎnxiè!", "Universal appreciation phrase.", listOf("不客气！", "不用谢！"))
                "de" -> Quad("Vielen Dank!", "Fee-len Dank", "Reply is 'Bitte sehr!'.", listOf("Bitte schön!", "Gern geschehen!"))
                else -> Quad("Thank you very much!", "", "Polite appreciation.", listOf("You're welcome!", "Anytime!"))
            }

            lower.contains("where") || lower.contains("station") || lower.contains("train") || lower.contains("metro") -> when (targetLang.code) {
                "ja" -> Quad("駅はどちらですか？道を教えていただけますか？", "Eki wa dochira desu ka? Michi o oshiete itadakemasu ka?", "People in Japan often walk a short distance with you to show the way.", listOf("あちらの角を右です。", "真っ直ぐ行ってください。"))
                "es" -> Quad("¿Dónde está la estación? ¿Me puede indicar el camino?", "Don-de es-tah la es-tah-syon?", "Locals will gladly point with directions.", listOf("Todo recto dos cuadras", "A la derecha"))
                "fr" -> Quad("Où se trouve la gare s'il vous plaît ?", "Oo suh troov lah gahr", "Always start with 'Excusez-moi'.", listOf("Tout droit au carrefour", "À gauche"))
                else -> Quad("Where is the station please?", "", "Navigation assistance query.", listOf("Straight ahead", "Take the first right"))
            }

            lower.contains("water") || lower.contains("bill") || lower.contains("menu") || lower.contains("check") -> when (targetLang.code) {
                "ja" -> Quad("お会計をお願いします。お水もいただけますか？", "O-kaikei o onegai shimasu. O-mizu mo itadakemasu ka?", "Pay at the front cashier register in most Japanese restaurants.", listOf("かしこまりました。", "合計で2,000円です。"))
                "es" -> Quad("La cuenta y un poco de agua, por favor.", "Lah kwen-tah por fah-vor", "Ask waiter by catching eye contact gently.", listOf("Enseguida se la traigo", "¿Desea pagar con tarjeta?"))
                "fr" -> Quad("L'addition s'il vous plaît, et une carafe d'eau.", "Lah-dee-syon seel voo play", "A 'carafe d'eau' is free fresh tap water in France.", listOf("Voilà monsieur/dame", "Vous payez par carte ?"))
                else -> Quad("The bill please, and some water.", "", "Dining request.", listOf("Right away", "Card or cash?"))
            }

            else -> {
                // General direct translation placeholder
                Quad(
                    "[$text] (${targetLang.displayName})",
                    "",
                    "Connect Gemini API Key in AI Studio Secrets for live neural AI translations with cultural nuance.",
                    listOf("Yes, understood", "Could you repeat that?")
                )
            }
        }

        return GeminiTranslationResponse(
            translatedText = translated,
            phonetic = phonetic,
            culturalNuance = nuance,
            suggestedReplies = replies,
            isAiGenerated = false
        )
    }

    private data class Quad(
        val translated: String,
        val phonetic: String,
        val nuance: String,
        val replies: List<String>
    )
}
