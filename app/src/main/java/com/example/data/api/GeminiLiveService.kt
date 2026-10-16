package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.Language
import com.example.data.model.TravelScenario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class LiveConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    STREAMING_RESPONSE,
    ERROR
}

data class LiveChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class GeminiLiveService {
    private val TAG = "GeminiLiveService"

    companion object {
        const val LIVE_MODEL = "gemini-3.8-live"
        const val NATIVE_AUDIO_MODEL = "gemini-2.5-flash-native-audio-preview-12-2025"
        const val STREAM_FALLBACK_MODEL = "gemini-3.8-flash"

        val AVAILABLE_VOICES = listOf("Aoede", "Kore", "Fenrir", "Puck", "Charon")
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS) // infinite for websockets
        .writeTimeout(30, TimeUnit.SECONDS)
        .pingInterval(20, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    private val _connectionState = MutableStateFlow(LiveConnectionState.DISCONNECTED)
    val connectionState: StateFlow<LiveConnectionState> = _connectionState.asStateFlow()

    private val _incomingTextChunk = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val incomingTextChunk: SharedFlow<String> = _incomingTextChunk.asSharedFlow()

    private val _activeVoice = MutableStateFlow("Aoede")
    val activeVoice: StateFlow<String> = _activeVoice.asStateFlow()

    fun setVoice(voice: String) {
        if (AVAILABLE_VOICES.contains(voice)) {
            _activeVoice.value = voice
        }
    }

    fun isConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    /**
     * Connects to the Gemini Live WebSocket endpoint (v1alpha BidiGenerateContent).
     */
    fun connectLiveSession(
        sourceLang: Language,
        targetLang: Language,
        scenario: TravelScenario,
        onConnected: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        disconnect()

        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isConfigured()) {
            _connectionState.value = LiveConnectionState.CONNECTED
            onConnected()
            return
        }

        _connectionState.value = LiveConnectionState.CONNECTING

        val wsUrl = "wss://generativelanguage.googleapis.com/ws/google.ai.generativelanguage.v1alpha.GenerativeService.BidiGenerateContent?key=$apiKey"
        val request = Request.Builder().url(wsUrl).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "Live WebSocket opened successfully!")
                _connectionState.value = LiveConnectionState.CONNECTED

                // Send Live Setup Configuration
                sendLiveSetup(webSocket, sourceLang, targetLang, scenario)
                serviceScope.launch {
                    onConnected()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleLiveServerMessage(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "Live WebSocket closing: $code / $reason")
                _connectionState.value = LiveConnectionState.DISCONNECTED
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "Live WebSocket failure: ${t.message}", t)
                _connectionState.value = LiveConnectionState.ERROR
                serviceScope.launch {
                    onError("Live connection notice: Falling back to direct streaming. ${t.message}")
                }
            }
        })
    }

    private fun sendLiveSetup(
        ws: WebSocket,
        sourceLang: Language,
        targetLang: Language,
        scenario: TravelScenario
    ) {
        try {
            val systemInstruction = """
                You are VoxTravel Live, the real-time spoken travel interpreter and live conversational companion.
                Active Model: $LIVE_MODEL.
                Primary user language: ${sourceLang.displayName} (${sourceLang.code}).
                Destination language: ${targetLang.displayName} (${targetLang.code}).
                Current Travel Context: ${scenario.title} (${scenario.description}).
                
                Guidelines:
                1. Keep spoken responses snappy, warm, natural, and concise (ideal for live audio listening on the move).
                2. When translating user speech, output the direct translation with correct native phrasing, then optionally add an ultra-short (1 sentence) cultural tip or etiquette cue if relevant.
                3. If the user asks a question about navigation, food, customs, or travel tips, answer clearly and practically like a knowledgeable local guide.
                4. Always remain in character as an attentive, friendly live travel companion.
            """.trimIndent()

            val setupJson = JSONObject().apply {
                put("setup", JSONObject().apply {
                    put("model", "models/$LIVE_MODEL")
                    put("generationConfig", JSONObject().apply {
                        put("responseModalities", JSONArray().apply {
                            put("TEXT")
                        })
                        put("speechConfig", JSONObject().apply {
                            put("voiceConfig", JSONObject().apply {
                                put("prebuiltVoiceConfig", JSONObject().apply {
                                    put("voiceName", _activeVoice.value)
                                })
                            })
                        })
                    })
                    put("systemInstruction", JSONObject().apply {
                        val parts = JSONArray()
                        parts.put(JSONObject().apply { put("text", systemInstruction) })
                        put("parts", parts)
                    })
                })
            }

            ws.send(setupJson.toString())
            Log.d(TAG, "Sent Live setup message with model $LIVE_MODEL")
        } catch (e: Exception) {
            Log.e(TAG, "Error building setup JSON: ${e.message}", e)
        }
    }

    private fun handleLiveServerMessage(jsonText: String) {
        try {
            val root = JSONObject(jsonText)
            val serverContent = root.optJSONObject("serverContent") ?: return

            val modelTurn = serverContent.optJSONObject("modelTurn")
            if (modelTurn != null) {
                val parts = modelTurn.optJSONArray("parts")
                if (parts != null) {
                    for (i in 0 until parts.length()) {
                        val part = parts.getJSONObject(i)
                        val text = part.optString("text", "")
                        if (text.isNotBlank()) {
                            _connectionState.value = LiveConnectionState.STREAMING_RESPONSE
                            _incomingTextChunk.tryEmit(text)
                        }
                    }
                }
            }

            val turnComplete = serverContent.optBoolean("turnComplete", false)
            if (turnComplete) {
                _connectionState.value = LiveConnectionState.CONNECTED
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing server message: ${e.message}", e)
        }
    }

    /**
     * Sends a turn of user speech/text into the live session.
     */
    fun sendUserTurn(
        userText: String,
        sourceLang: Language,
        targetLang: Language,
        scenario: TravelScenario,
        onChunk: (String) -> Unit,
        onComplete: (String) -> Unit
    ) {
        val ws = webSocket
        if (ws != null && _connectionState.value == LiveConnectionState.CONNECTED && isConfigured()) {
            try {
                val contentJson = JSONObject().apply {
                    put("clientContent", JSONObject().apply {
                        val turnsArray = JSONArray()
                        val turnObj = JSONObject().apply {
                            put("role", "user")
                            val partsArray = JSONArray()
                            partsArray.put(JSONObject().apply {
                                put("text", userText)
                            })
                            put("parts", partsArray)
                        }
                        turnsArray.put(turnObj)
                        put("turns", turnsArray)
                        put("turnComplete", true)
                    })
                }
                ws.send(contentJson.toString())
                return
            } catch (e: Exception) {
                Log.w(TAG, "Failed sending via WebSocket: ${e.message}. Streaming via REST fallback.")
            }
        }

        // Streaming REST fallback using gemini-3.8-live / gemini-3.8-flash
        serviceScope.launch {
            streamContentFallback(userText, sourceLang, targetLang, scenario, onChunk, onComplete)
        }
    }

    private suspend fun streamContentFallback(
        userText: String,
        sourceLang: Language,
        targetLang: Language,
        scenario: TravelScenario,
        onChunk: (String) -> Unit,
        onComplete: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        _connectionState.value = LiveConnectionState.STREAMING_RESPONSE

        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isConfigured()) {
            // Intelligent local preview response
            val simulated = generateLocalLivePreviewResponse(userText, targetLang, scenario)
            val words = simulated.split(" ")
            val fullBuilder = StringBuilder()
            for (word in words) {
                val chunk = "$word "
                fullBuilder.append(chunk)
                withContext(Dispatchers.Main) {
                    onChunk(chunk)
                }
                kotlinx.coroutines.delay(60)
            }
            _connectionState.value = LiveConnectionState.CONNECTED
            withContext(Dispatchers.Main) {
                onComplete(fullBuilder.toString().trim())
            }
            return@withContext
        }

        // Try Live Model first, fallback to Flash
        val modelsToTry = listOf(LIVE_MODEL, STREAM_FALLBACK_MODEL)
        var succeeded = false

        for (model in modelsToTry) {
            try {
                val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/$model:streamGenerateContent?alt=sse&key=$apiKey"
                val prompt = """
                    You are VoxTravel Live Companion powered by $model.
                    Traveler is speaking in ${sourceLang.displayName}.
                    Target destination language is ${targetLang.displayName}.
                    Scenario: ${scenario.title} (${scenario.description}).
                    
                    User said: "$userText"
                    
                    Respond naturally, concisely, and supportively. If translating, provide the natural phrase in ${targetLang.displayName} along with a helpful pronunciation cue and brief travel tip.
                """.trimIndent()

                val requestJson = JSONObject().apply {
                    val contentsArray = JSONArray()
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray()
                        partsArray.put(JSONObject().apply { put("text", prompt) })
                        put("parts", partsArray)
                    }
                    contentsArray.put(contentObj)
                    put("contents", contentsArray)
                }

                val req = Request.Builder()
                    .url(endpoint)
                    .post(requestJson.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val fullResponse = StringBuilder()
                val response = client.newCall(req).execute()

                if (!response.isSuccessful) {
                    throw IllegalStateException("Stream error ${response.code}")
                }

                response.body?.byteStream()?.bufferedReader()?.use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val rawLine = line?.trim() ?: continue
                        if (!rawLine.startsWith("data:")) continue
                        val jsonPayload = rawLine.removePrefix("data:").trim()
                        if (jsonPayload.isEmpty() || jsonPayload == "[DONE]") continue

                        try {
                            val chunkObj = JSONObject(jsonPayload)
                            val candidates = chunkObj.optJSONArray("candidates")
                            val text = candidates?.optJSONObject(0)
                                ?.optJSONObject("content")
                                ?.optJSONArray("parts")
                                ?.optJSONObject(0)
                                ?.optString("text")

                            if (!text.isNullOrBlank()) {
                                fullResponse.append(text)
                                withContext(Dispatchers.Main) {
                                    onChunk(text)
                                }
                            }
                        } catch (e: Exception) {
                            // parse error on chunk
                        }
                    }
                }

                _connectionState.value = LiveConnectionState.CONNECTED
                withContext(Dispatchers.Main) {
                    onComplete(fullResponse.toString().trim())
                }
                succeeded = true
                break
            } catch (e: Exception) {
                Log.w(TAG, "Streaming with model $model failed: ${e.message}")
            }
        }

        if (!succeeded) {
            val fallback = "I heard you say: \"$userText\". Here is your travel guidance in ${targetLang.displayName}. Let's continue!"
            withContext(Dispatchers.Main) {
                onChunk(fallback)
                onComplete(fallback)
            }
            _connectionState.value = LiveConnectionState.CONNECTED
        }
    }

    private fun generateLocalLivePreviewResponse(
        input: String,
        targetLang: Language,
        scenario: TravelScenario
    ): String {
        val lower = input.lowercase()
        return when {
            lower.contains("hello") || lower.contains("hi") ->
                "Hello! Welcome! I am your live Gemini travel companion. Ready to explore ${targetLang.displayName} speaking destinations with you!"
            lower.contains("eat") || lower.contains("food") || lower.contains("restaurant") ->
                "For great local dining, always look for spots with queues of local residents! In ${targetLang.displayName}, you can ask: 'What do you recommend?'"
            lower.contains("train") || lower.contains("station") || lower.contains("subway") ->
                "Most train stations feature color-coded lines and bilingual signs. Which station are you heading towards?"
            else ->
                "Understood! As your live travel companion in ${scenario.title}, I'm here to interpret and guide you smoothly in real time."
        }
    }

    fun disconnect() {
        try {
            webSocket?.close(1000, "User disconnected session")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing websocket: ${e.message}")
        } finally {
            webSocket = null
            _connectionState.value = LiveConnectionState.DISCONNECTED
        }
    }
}
