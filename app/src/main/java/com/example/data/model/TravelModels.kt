package com.example.data.model

import java.util.Locale
import java.util.UUID

enum class SpeakerSide {
    TRAVELER,
    LOCAL
}

data class Language(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flag: String,
    val localeTag: String
) {
    val locale: Locale
        get() = Locale.forLanguageTag(localeTag)

    companion object {
        val ALL = listOf(
            Language("en", "English", "English", "🇺🇸", "en-US"),
            Language("ja", "Japanese", "日本語", "🇯🇵", "ja-JP"),
            Language("es", "Spanish", "Español", "🇪🇸", "es-ES"),
            Language("fr", "French", "Français", "🇫🇷", "fr-FR"),
            Language("zh", "Chinese", "中文", "🇨🇳", "zh-CN"),
            Language("de", "German", "Deutsch", "🇩🇪", "de-DE"),
            Language("it", "Italian", "Italiano", "🇮🇹", "it-IT"),
            Language("ko", "Korean", "한국어", "🇰🇷", "ko-KR"),
            Language("pt", "Portuguese", "Português", "🇧🇷", "pt-BR"),
            Language("hi", "Hindi", "हिन्दी", "🇮🇳", "hi-IN"),
            Language("ar", "Arabic", "العربية", "🇸🇦", "ar-SA"),
            Language("th", "Thai", "ไทย", "🇹🇭", "th-TH"),
            Language("vi", "Vietnamese", "Tiếng Việt", "🇻🇳", "vi-VN"),
            Language("tr", "Turkish", "Türkçe", "🇹🇷", "tr-TR"),
            Language("id", "Indonesian", "Bahasa", "🇮🇩", "id-ID"),
            Language("nl", "Dutch", "Nederlands", "🇳🇱", "nl-NL"),
            Language("el", "Greek", "Ελληνικά", "🇬🇷", "el-GR")
        )

        val DEFAULT_TRAVELER = ALL[0] // English
        val DEFAULT_LOCAL = ALL[1]    // Japanese
    }
}

enum class TravelScenario(val title: String, val icon: String, val description: String) {
    GENERAL("General", "💬", "Everyday casual travel conversation"),
    FOOD_DINING("Dining & Allergies", "🍜", "Menu queries, dietary needs, bill asking"),
    TRANSIT_DIRECTIONS("Transit & Maps", "🚕", "Taxi, subways, train stations, navigating"),
    HOTEL_LODGING("Lodging & Stay", "🏨", "Check-in, room amenities, luggage storage"),
    SHOPPING("Markets & Shops", "🛍️", "Pricing, sizes, bargaining, tax-free"),
    EMERGENCY("Medical & Urgency", "🚨", "Allergies, police, pharmacy, embassy")
}

data class TranslationResult(
    val id: String = UUID.randomUUID().toString(),
    val speaker: SpeakerSide,
    val sourceText: String,
    val sourceLang: Language,
    val targetText: String,
    val targetLang: Language,
    val phonetic: String = "",
    val culturalNuance: String = "",
    val suggestedReplies: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false
)

data class TravelPhrase(
    val id: String,
    val scenario: TravelScenario,
    val english: String,
    val translation: String,
    val phonetic: String,
    val isEmergency: Boolean = false,
    val dietaryAlert: Boolean = false
)

data class CountryEtiquette(
    val country: String,
    val flag: String,
    val languageCode: String,
    val greetings: String,
    val tippingCustom: String,
    val diningRules: List<String>,
    val etiquetteTips: List<String>,
    val taboos: List<String>,
    val emergencyNumbers: String
)
