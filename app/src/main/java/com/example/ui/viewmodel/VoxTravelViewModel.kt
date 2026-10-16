package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioSpeechManager
import com.example.data.api.GeminiLiveService
import com.example.data.api.GeminiTranslationService
import com.example.data.api.LiveChatMessage
import com.example.data.api.LiveConnectionState
import com.example.data.model.CountryEtiquette
import com.example.data.model.Language
import com.example.data.model.SpeakerSide
import com.example.data.model.TravelPhrase
import com.example.data.model.TravelScenario
import com.example.data.model.TranslationResult
import com.example.data.repository.TravelDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val label: String, val icon: String) {
    LIVE_TALK("Live Talk", "🎙️"),
    GEMINI_LIVE("Live AI", "✨"),
    PHRASEBOOK("Phrases", "📖"),
    CULTURE("Culture", "🏯"),
    SAVED("Saved", "⭐")
}

data class VoxTravelUiState(
    val activeTab: AppTab = AppTab.LIVE_TALK,
    val travelerLang: Language = Language.DEFAULT_TRAVELER,
    val localLang: Language = Language.DEFAULT_LOCAL,
    val activeScenario: TravelScenario = TravelScenario.GENERAL,
    val conversations: List<TranslationResult> = emptyList(),
    val isListening: Boolean = false,
    val listeningSpeaker: SpeakerSide? = null,
    val interimSpeechText: String = "",
    val isSpeakingTts: Boolean = false,
    val isTranslating: Boolean = false,
    val audioVolumeRms: Float = 0f,
    val autoSpeakEnabled: Boolean = true,
    val isDualViewFlipped: Boolean = false, // 180° rotation for face-to-face table mode
    val speechRate: Float = 0.9f,
    val isLanguageDialogVisible: Boolean = false,
    val languageDialogTarget: SpeakerSide? = null,
    val activeFullscreenPhrase: TravelPhrase? = null,
    val savedBookmarks: Set<String> = emptySet(),
    val statusMessage: String? = null,
    val isApiKeyConfigured: Boolean = false,

    // Gemini 3.8 Live API states
    val liveConnectionState: LiveConnectionState = LiveConnectionState.DISCONNECTED,
    val liveChatMessages: List<LiveChatMessage> = emptyList(),
    val liveCurrentStreamChunk: String = "",
    val isLiveSessionActive: Boolean = false,
    val liveActiveVoice: String = "Aoede",
    val isHandsFreeLiveLoop: Boolean = true,
    val isLiveUserSpeaking: Boolean = false
)

class VoxTravelViewModel(application: Application) : AndroidViewModel(application) {

    val audioManager = AudioSpeechManager(application)
    val liveService = GeminiLiveService()

    private val _uiState = MutableStateFlow(VoxTravelUiState())
    val uiState: StateFlow<VoxTravelUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                isApiKeyConfigured = GeminiTranslationService.isApiKeyConfigured() || liveService.isConfigured(),
                conversations = getInitialWelcomeConversation(),
                liveChatMessages = getInitialLiveWelcomeMessages()
            )
        }

        // Collect audio states from AudioManager
        viewModelScope.launch {
            audioManager.isSpeaking.collect { speaking ->
                val wasSpeaking = _uiState.value.isSpeakingTts
                _uiState.update { it.copy(isSpeakingTts = speaking) }

                // Hands-free continuous loop: if Gemini just finished speaking, auto-listen for the traveler's next question!
                if (wasSpeaking && !speaking && _uiState.value.isLiveSessionActive && _uiState.value.isHandsFreeLiveLoop && _uiState.value.activeTab == AppTab.GEMINI_LIVE) {
                    startLiveVoiceInput()
                }
            }
        }

        viewModelScope.launch {
            audioManager.isListening.collect { listening ->
                _uiState.update {
                    if (!listening && it.isListening) {
                        it.copy(isListening = false, listeningSpeaker = null, isLiveUserSpeaking = false)
                    } else {
                        it.copy(isListening = listening)
                    }
                }
            }
        }

        viewModelScope.launch {
            audioManager.audioVolume.collect { volume ->
                _uiState.update { it.copy(audioVolumeRms = volume) }
            }
        }

        // Collect Live API connection states
        viewModelScope.launch {
            liveService.connectionState.collect { connState ->
                _uiState.update { it.copy(liveConnectionState = connState) }
            }
        }

        // Collect streaming text chunks from Live API
        viewModelScope.launch {
            liveService.incomingTextChunk.collect { chunk ->
                _uiState.update { current ->
                    current.copy(liveCurrentStreamChunk = current.liveCurrentStreamChunk + chunk)
                }
            }
        }
    }

    private fun getInitialWelcomeConversation(): List<TranslationResult> {
        return listOf(
            TranslationResult(
                id = "welcome_1",
                speaker = SpeakerSide.TRAVELER,
                sourceText = "Hello! Can you help me find the nearest train station?",
                sourceLang = Language.DEFAULT_TRAVELER,
                targetText = "こんにちは！一番近い駅への道を教えていただけますか？",
                targetLang = Language.DEFAULT_LOCAL,
                phonetic = "Konnichiwa! Ichiban chikai eki e no michi o oshiete itadakemasu ka?",
                culturalNuance = "In Japan, a polite bow and beginning with 'Sumimasen' (excuse me) opens any conversation warmly.",
                suggestedReplies = listOf("はい、あちらを右です。", "真っ直ぐ行ってください。"),
                timestamp = System.currentTimeMillis() - 60000
            ),
            TranslationResult(
                id = "welcome_2",
                speaker = SpeakerSide.LOCAL,
                sourceText = "あそこの交差点を右に曲がると、すぐ地下鉄の入口がありますよ。",
                sourceLang = Language.DEFAULT_LOCAL,
                targetText = "If you turn right at that intersection over there, there is a subway entrance right away.",
                targetLang = Language.DEFAULT_TRAVELER,
                phonetic = "Asoko no kōsaten o migi ni magaru to, sugu chikatetsu no iriguchi ga arimasu yo.",
                culturalNuance = "Locals are often eager to guide travelers, sometimes even walking with you for a block.",
                suggestedReplies = listOf("Thank you very much!", "How long does it take on foot?"),
                timestamp = System.currentTimeMillis() - 30000
            )
        )
    }

    private fun getInitialLiveWelcomeMessages(): List<LiveChatMessage> {
        return listOf(
            LiveChatMessage(
                isUser = false,
                text = "Hello traveler! I am your live AI companion powered by Gemini 3.8 Live. Tap the live mic or start a session to talk naturally in real-time about directions, language nuances, or ordering food!"
            )
        )
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(activeTab = tab) }
        if (tab == AppTab.GEMINI_LIVE && !_uiState.value.isLiveSessionActive) {
            startLiveSession()
        }
    }

    fun setScenario(scenario: TravelScenario) {
        _uiState.update { it.copy(activeScenario = scenario) }
        if (_uiState.value.isLiveSessionActive) {
            // reconnect with updated scenario
            startLiveSession()
        }
    }

    fun toggleDualViewFlip() {
        _uiState.update { it.copy(isDualViewFlipped = !it.isDualViewFlipped) }
    }

    fun toggleAutoSpeak() {
        _uiState.update { it.copy(autoSpeakEnabled = !it.autoSpeakEnabled) }
    }

    fun setSpeechRate(rate: Float) {
        _uiState.update { it.copy(speechRate = rate) }
    }

    fun openLanguagePicker(side: SpeakerSide) {
        _uiState.update {
            it.copy(
                isLanguageDialogVisible = true,
                languageDialogTarget = side
            )
        }
    }

    fun closeLanguagePicker() {
        _uiState.update {
            it.copy(
                isLanguageDialogVisible = false,
                languageDialogTarget = null
            )
        }
    }

    fun selectLanguage(language: Language) {
        val target = _uiState.value.languageDialogTarget
        _uiState.update { current ->
            when (target) {
                SpeakerSide.TRAVELER -> current.copy(travelerLang = language, isLanguageDialogVisible = false)
                SpeakerSide.LOCAL -> current.copy(localLang = language, isLanguageDialogVisible = false)
                null -> current.copy(isLanguageDialogVisible = false)
            }
        }
        if (_uiState.value.isLiveSessionActive) {
            startLiveSession()
        }
    }

    fun swapLanguages() {
        _uiState.update { current ->
            current.copy(
                travelerLang = current.localLang,
                localLang = current.travelerLang
            )
        }
        if (_uiState.value.isLiveSessionActive) {
            startLiveSession()
        }
    }

    // --- Face-to-Face Dual Talk Voice Input ---
    fun startVoiceInput(speaker: SpeakerSide) {
        val currentState = _uiState.value
        if (currentState.isListening) {
            audioManager.stopListening()
            _uiState.update { it.copy(isListening = false, listeningSpeaker = null, interimSpeechText = "") }
            return
        }

        audioManager.stopSpeaking()
        val speechLang = if (speaker == SpeakerSide.TRAVELER) currentState.travelerLang else currentState.localLang

        _uiState.update {
            it.copy(
                isListening = true,
                listeningSpeaker = speaker,
                interimSpeechText = "Listening in ${speechLang.displayName}...",
                statusMessage = null
            )
        }

        audioManager.startListening(
            language = speechLang,
            onPartialResult = { partial ->
                _uiState.update { it.copy(interimSpeechText = partial) }
            },
            onFinalResult = { text ->
                _uiState.update { it.copy(interimSpeechText = "", isListening = false, listeningSpeaker = null) }
                processTranslation(text, speaker)
            },
            onError = { error ->
                _uiState.update {
                    it.copy(
                        isListening = false,
                        listeningSpeaker = null,
                        interimSpeechText = "",
                        statusMessage = error
                    )
                }
            }
        )
    }

    fun stopVoiceInput() {
        audioManager.stopListening()
        _uiState.update { it.copy(isListening = false, listeningSpeaker = null, interimSpeechText = "", isLiveUserSpeaking = false) }
    }

    fun processTranslation(text: String, speaker: SpeakerSide) {
        if (text.isBlank()) return

        val state = _uiState.value
        val sourceLang = if (speaker == SpeakerSide.TRAVELER) state.travelerLang else state.localLang
        val targetLang = if (speaker == SpeakerSide.TRAVELER) state.localLang else state.travelerLang

        _uiState.update { it.copy(isTranslating = true, statusMessage = null) }

        viewModelScope.launch {
            try {
                val response = GeminiTranslationService.translateConversation(
                    text = text,
                    sourceLang = sourceLang,
                    targetLang = targetLang,
                    speaker = speaker,
                    scenario = state.activeScenario
                )

                val result = TranslationResult(
                    speaker = speaker,
                    sourceText = text,
                    sourceLang = sourceLang,
                    targetText = response.translatedText,
                    targetLang = targetLang,
                    phonetic = response.phonetic,
                    culturalNuance = response.culturalNuance,
                    suggestedReplies = response.suggestedReplies,
                    timestamp = System.currentTimeMillis()
                )

                _uiState.update { current ->
                    current.copy(
                        conversations = listOf(result) + current.conversations,
                        isTranslating = false
                    )
                }

                // If AutoSpeak is on, audibly speak out the translation immediately!
                if (state.autoSpeakEnabled && response.translatedText.isNotBlank()) {
                    speakText(response.translatedText, targetLang)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isTranslating = false,
                        statusMessage = "Translation failed: ${e.message}"
                    )
                }
            }
        }
    }

    // --- Gemini 3.8 Live API Controls ---
    fun startLiveSession() {
        _uiState.update { it.copy(isLiveSessionActive = true) }
        liveService.connectLiveSession(
            sourceLang = _uiState.value.travelerLang,
            targetLang = _uiState.value.localLang,
            scenario = _uiState.value.activeScenario,
            onConnected = {
                _uiState.update { it.copy(liveConnectionState = LiveConnectionState.CONNECTED) }
            },
            onError = { errMsg ->
                _uiState.update { it.copy(statusMessage = errMsg) }
            }
        )
    }

    fun stopLiveSession() {
        audioManager.stopSpeaking()
        audioManager.stopListening()
        liveService.disconnect()
        _uiState.update {
            it.copy(
                isLiveSessionActive = false,
                isLiveUserSpeaking = false,
                liveConnectionState = LiveConnectionState.DISCONNECTED
            )
        }
    }

    fun setLiveVoice(voice: String) {
        liveService.setVoice(voice)
        _uiState.update { it.copy(liveActiveVoice = voice) }
    }

    fun toggleHandsFreeLiveLoop() {
        _uiState.update { it.copy(isHandsFreeLiveLoop = !it.isHandsFreeLiveLoop) }
    }

    fun startLiveVoiceInput() {
        audioManager.stopSpeaking()
        val speechLang = _uiState.value.travelerLang

        _uiState.update {
            it.copy(
                isListening = true,
                isLiveUserSpeaking = true,
                interimSpeechText = "Listening live...",
                liveCurrentStreamChunk = ""
            )
        }

        audioManager.startListening(
            language = speechLang,
            onPartialResult = { partial ->
                _uiState.update { it.copy(interimSpeechText = partial) }
            },
            onFinalResult = { text ->
                _uiState.update {
                    it.copy(
                        interimSpeechText = "",
                        isListening = false,
                        isLiveUserSpeaking = false
                    )
                }
                sendLiveMessage(text)
            },
            onError = { err ->
                _uiState.update {
                    it.copy(
                        isListening = false,
                        isLiveUserSpeaking = false,
                        interimSpeechText = "",
                        statusMessage = err
                    )
                }
            }
        )
    }

    fun toggleLiveMic() {
        if (_uiState.value.isListening) {
            stopVoiceInput()
        } else {
            startLiveVoiceInput()
        }
    }

    fun sendLiveMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = LiveChatMessage(isUser = true, text = userText)
        _uiState.update { current ->
            current.copy(
                liveChatMessages = current.liveChatMessages + userMessage,
                liveCurrentStreamChunk = ""
            )
        }

        val state = _uiState.value
        val streamingBuffer = StringBuilder()

        liveService.sendUserTurn(
            userText = userText,
            sourceLang = state.travelerLang,
            targetLang = state.localLang,
            scenario = state.activeScenario,
            onChunk = { chunk ->
                streamingBuffer.append(chunk)
                _uiState.update { it.copy(liveCurrentStreamChunk = streamingBuffer.toString()) }
            },
            onComplete = { fullResponse ->
                val aiMessage = LiveChatMessage(isUser = false, text = fullResponse)
                _uiState.update { current ->
                    current.copy(
                        liveChatMessages = current.liveChatMessages + aiMessage,
                        liveCurrentStreamChunk = ""
                    )
                }

                // Spoken TTS output from Gemini
                if (state.autoSpeakEnabled && fullResponse.isNotBlank()) {
                    speakText(fullResponse, state.travelerLang)
                }
            }
        )
    }

    fun speakText(text: String, language: Language) {
        audioManager.speak(text, language, _uiState.value.speechRate)
    }

    fun stopSpeech() {
        audioManager.stopSpeaking()
    }

    fun toggleBookmark(id: String) {
        _uiState.update { current ->
            val updatedBookmarks = if (current.savedBookmarks.contains(id)) {
                current.savedBookmarks - id
            } else {
                current.savedBookmarks + id
            }
            val updatedConversations = current.conversations.map {
                if (it.id == id) it.copy(isBookmarked = !it.isBookmarked) else it
            }
            current.copy(savedBookmarks = updatedBookmarks, conversations = updatedConversations)
        }
    }

    fun showFullscreenPhrase(phrase: TravelPhrase?) {
        _uiState.update { it.copy(activeFullscreenPhrase = phrase) }
    }

    fun clearHistory() {
        _uiState.update { it.copy(conversations = emptyList()) }
    }

    fun clearLiveChat() {
        _uiState.update { it.copy(liveChatMessages = emptyList(), liveCurrentStreamChunk = "") }
    }

    fun dismissStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }

    fun getEtiquetteForCurrentCountry(): CountryEtiquette {
        return TravelDataRepository.getEtiquetteForLanguage(_uiState.value.localLang.code)
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.destroy()
        liveService.disconnect()
    }
}
