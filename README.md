# ✈️ VoxTravel — Real-Time Voice Translator & Gemini 3.8 Live Companion

<div align="center">
  <img src="app/src/main/res/drawable/vox_travel_hero_1792168351336.jpg" alt="VoxTravel Hero Banner" width="100%" style="border-radius: 16px; margin-bottom: 20px;" />
  
  <p align="center">
    <strong>A next-generation real-time voice interpreter, face-to-face dual conversation translator, and interactive travel companion powered by Gemini 3.8 Live API.</strong>
  </p>

  <p align="center">
    <img src="https://img.shields.io/badge/Model-Gemini%203.8%20Live-00E5FF?style=for-the-badge&logo=google" alt="Gemini 3.8 Live" />
    <img src="https://img.shields.io/badge/Fallback-Gemini%203.8%20Flash-0077FE?style=for-the-badge&logo=google" alt="Gemini 3.8 Flash" />
    <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
    <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  </p>
</div>

---

## 📖 Overview

**VoxTravel** is an Android application engineered for travelers traversing foreign countries. It breaks down language and cultural barriers through **live voice conversations** using the **Gemini 3.8 Live API**, **dual face-to-face table translation with 180° rotation**, **audible native text-to-speech**, **life-saving dietary allergy alert cards**, and **local cultural etiquette guides**.

---

## 🏛️ System Architecture

The following diagram illustrates the component architecture of VoxTravel:

```mermaid
graph TD
    subgraph UI_Layer["🎨 Presentation Layer (Jetpack Compose & Material 3)"]
        MainActivity["MainActivity (Edge-to-Edge Navigation)"]
        LiveDualTalk["LiveDualTalkScreen<br/>(Face-to-Face Dual Mic & 180° Flip)"]
        GeminiLive["GeminiLiveScreen<br/>(Animated Iridescent Orb & Live Voice Loop)"]
        Phrasebook["PhrasebookScreen<br/>(Travel Scenarios & Allergy Cards)"]
        CultureGuide["CultureGuideScreen<br/>(Local Etiquette, Tipping & Taboos)"]
        SavedPhrases["SavedPhrasesScreen<br/>(Offline Bookmarks & Audio Playback)"]
    end

    subgraph Audio_Layer["🔊 Audio & Speech Engine"]
        AudioSpeechMgr["AudioSpeechManager"]
        STT["Android SpeechRecognizer<br/>(RMS dB Audio Volume Stream)"]
        TTS["Android TextToSpeech<br/>(Variable Speed & Native Dialects)"]
        WaveVis["AudioWaveVisualizer<br/>(Dynamic Waveform Feedback)"]
    end

    subgraph State_Layer["⚡ State & Business Logic"]
        ViewModel["VoxTravelViewModel<br/>(StateFlow & Coroutine Scopes)"]
    end

    subgraph Data_Layer["🌐 Networking & Gemini AI"]
        LiveService["GeminiLiveService<br/>(WebSocket BidiGenerateContent & SSE Stream)"]
        TranslationService["GeminiTranslationService<br/>(OkHttp 60s Client & Structured JSON)"]
        DataRepo["TravelDataRepository<br/>(17+ Languages, Cultural Rules, Emergency Data)"]
    end

    subgraph Cloud_Backend["☁️ Google Gemini Cloud"]
        GeminiLiveModel["gemini-3.8-live<br/>(Bidirectional Live Audio/Text)"]
        GeminiFlashModel["gemini-3.8-flash / gemini-3.5-flash<br/>(Contextual Translation & Nuance)"]
    end

    MainActivity --> LiveDualTalk
    MainActivity --> GeminiLive
    MainActivity --> Phrasebook
    MainActivity --> CultureGuide
    MainActivity --> SavedPhrases

    LiveDualTalk --> ViewModel
    GeminiLive --> ViewModel
    Phrasebook --> ViewModel
    CultureGuide --> ViewModel
    SavedPhrases --> ViewModel

    ViewModel --> AudioSpeechMgr
    AudioSpeechMgr --> STT
    AudioSpeechMgr --> TTS
    AudioSpeechMgr --> WaveVis

    ViewModel --> LiveService
    ViewModel --> TranslationService
    ViewModel --> DataRepo

    LiveService --> GeminiLiveModel
    TranslationService --> GeminiFlashModel
```

---

## 🔄 Sequence Diagrams

### 1. Gemini 3.8 Live API Bidirectional Voice Conversation Flow

This diagram demonstrates how hands-free continuous voice interaction works with `gemini-3.8-live`:

```mermaid
sequenceDiagram
    autonumber
    actor Traveler as 🛫 Traveler
    participant App as 📱 GeminiLiveScreen
    participant Audio as 🎙️ AudioSpeechManager
    participant VM as ⚡ VoxTravelViewModel
    participant LiveSvc as 🌐 GeminiLiveService
    participant Gemini as ☁️ gemini-3.8-live

    Traveler->>App: Taps Live Orb or Speaks Query
    App->>Audio: startListening(travelerLang)
    Audio->>App: Streams audioVolumeRms (Orb expands & pulses)
    Traveler->>Audio: "Where can I find the best ramen nearby?"
    Audio->>VM: onFinalResult(recognizedSpeech)
    VM->>LiveSvc: sendUserTurn(text, travelerLang, localLang, scenario)
    
    alt WebSocket Session Active
        LiveSvc->>Gemini: BidiGenerateContent WebSocket Turn
    else Streaming REST Fallback
        LiveSvc->>Gemini: POST streamGenerateContent?alt=sse
    end

    Gemini-->>LiveSvc: Stream tokens (chunk by chunk)
    LiveSvc-->>VM: incomingTextChunk(token)
    VM-->>App: Updates liveCurrentStreamChunk (real-time UI text reveal)
    Gemini-->>LiveSvc: turnComplete
    LiveSvc-->>VM: onComplete(fullResponse)
    VM->>Audio: speak(fullResponse, travelerLang, speechRate)
    Audio->>Traveler: 🔊 Speaks answer in natural voice (Aoede/Kore/Fenrir)
    
    opt Continuous Hands-Free Loop Enabled
        Audio-->>VM: onDone (Gemini finished vocalizing)
        VM->>Audio: startListening(travelerLang) (Auto-resumes listening)
        Note over Traveler,Audio: Traveler can speak again without touching phone!
    end
```

---

### 2. Dual Face-to-Face Interpretation Flow with 180° Table Flip

This diagram illustrates how two people sitting across a table communicate in real time:

```mermaid
sequenceDiagram
    autonumber
    actor Traveler as 🛫 Traveler (English 🇺🇸)
    actor Local as 📍 Local Resident (Japanese 🇯🇵)
    participant UI as 📱 LiveDualTalkScreen
    participant Audio as 🎙️ AudioSpeechManager
    participant VM as ⚡ VoxTravelViewModel
    participant Gemini as ☁️ Gemini AI

    Note over UI: Table Flip Mode enabled: Local's view rotated 180°
    Traveler->>UI: Taps Cyan Mic ("Traveler")
    UI->>Audio: startListening(English)
    Traveler->>Audio: "Excuse me, how much is this ticket to Shibuya?"
    Audio->>VM: recognized text
    VM->>Gemini: translateConversation(text, "en" -> "ja", Dining/Transit)
    Gemini-->>VM: JSON (translatedText, phonetic, culturalNuance, quickReplies)
    VM->>Audio: speak("渋谷までの切符はいくらですか？", Japanese)
    Audio->>Local: 🔊 Verbal output in native Japanese
    UI->>Local: Displays Japanese card facing Local (flipped 180°)
    UI->>Traveler: Shows Romaji pronunciation & bowing etiquette tip
    Local->>UI: Taps Amber Mic ("Local Native")
    Local->>Audio: "２００円ですよ。あそこの券売機で買えます。"
    Audio->>VM: recognized text
    VM->>Gemini: translateConversation(text, "ja" -> "en")
    Gemini-->>VM: "It's 200 yen. You can buy it at the ticket machine over there."
    VM->>Audio: speak in English
    Audio->>Traveler: 🔊 Verbal English audio played aloud
```

---

## 🌟 Feature Breakdown

### 1. 🎙️ Gemini 3.8 Live Voice Companion (`gemini-3.8-live`)
- **Full-Duplex Live Streaming**: Real-time bidirectional communication powered by `gemini-3.8-live` with fallback to `gemini-2.5-flash-native-audio-preview-12-2025` and `gemini-3.8-flash`.
- **Animated 3D Iridescent Orb**: A centerpiece visual asset that rotates, pulses, and scales with user speech RMS audio volume.
- **Hands-Free Conversational Loop**: Automatic continuous listening after Gemini finishes speaking for natural, hands-free conversation on the go.
- **Voice Persona Selection**: Choose from 5 voices (`Aoede`, `Kore`, `Fenrir`, `Puck`, `Charon`).
- **One-Tap Travel Shortcuts**: Situational prompts for ordering food, subway navigation, and cultural advice.

### 2. 👥 Face-to-Face Dual Conversation Interpreter
- **Dedicated Split Buttons**: High-contrast, color-coded buttons for Traveler (Cyan) and Local Native Speaker (Amber).
- **180° Table Flip Mode**: Rotates conversation bubbles upside-down so someone seated across a restaurant table or checkout counter can read in their orientation.
- **Dynamic Waveform Visualizer**: Visual audio bars that pulse with speech volume.
- **Phonetic Pronunciation Guides**: Romanized pronunciation (Romaji, Pinyin, phonetics) for non-Latin alphabets.
- **Cultural Nuance Badges**: Real-time etiquette advice for each turn (e.g., bowing in Japan, formal *usted* in Spanish, bread placement in France).
- **Smart Quick Replies**: Tap contextually suggested response chips to keep conversations moving.
- **Silent Input Fallback**: Dual-flag keyboard input for noisy transit stations or quiet museums.

### 3. 🚨 Dietary Allergy Warning & Travel Phrasebook
- **Full-Screen Emergency Cards**: High-contrast, large-text allergy warnings (Peanuts, Celiac/Gluten, Shellfish, Dairy, Vegetarian) formatted to hand directly to foreign waitstaff or chefs.
- **6 Travel Scenarios**: Transit & Directions (🚕), Dining & Allergies (🍜), Lodging & Hotels (🏨), Shopping & Markets (🛍️), Emergency & Medical (🚨), and Casual Chat (💬).
- **Instant Audio Pronunciation**: Tap any phrase for native speech synthesis.

### 4. 🏯 Cultural Etiquette & Destination Guides
- Country-specific etiquette guides for **Japan, France, Italy, Spain, South Korea, Thailand, Germany, and Mexico**.
- Comprehensive travel guidance on **Tipping Customs, Dining Manners, Transit Tips, Taboos to Avoid**, and **Local Emergency Numbers** (Police, Ambulance, Fire, Tourist Police).

### 5. ⭐ Bookmarked Offline Expressions
- Star and save any translated exchange for quick offline access and one-touch audio replay without network connectivity.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Language** | Kotlin 2.2.10 |
| **UI Framework** | Jetpack Compose (BOM 2024.09.00), Material Design 3 (M3) |
| **AI Models** | `gemini-3.8-live` (Live API), `gemini-3.8-flash`, `gemini-3.5-flash` |
| **Networking** | OkHttp 4.10.0 (WebSocket & SSE Streaming), Retrofit 2.12.0, Moshi |
| **Audio & Speech** | Android `SpeechRecognizer`, Android `TextToSpeech` (TTS) |
| **Architecture** | MVVM (Model-View-ViewModel), StateFlow, Coroutines |
| **Testing** | Robolectric 4.16.1, JUnit 4, Compose UI Test |
| **Secret Management** | Google Secrets Gradle Plugin + `.env` / `.env.example` |

---

## 📁 Project Structure

```
├── .env.example                               # Environment template for GEMINI_API_KEY
├── app/
│   ├── build.gradle.kts                       # App Gradle build configuration & dependencies
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml            # Manifest declarations & audio permissions
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt            # Root Activity & navigation scaffolding
│   │   │   │   ├── audio/
│   │   │   │   │   └── AudioSpeechManager.kt  # STT (SpeechRecognizer) & TTS engine
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── GeminiLiveService.kt        # WebSocket & SSE Live API client
│   │   │   │   │   │   └── GeminiTranslationService.kt # Direct REST structured translator
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── TravelModels.kt             # Language, Scenario, and Result models
│   │   │   │   │   └── repository/
│   │   │   │   │       └── TravelDataRepository.kt     # Phrasebook & Country etiquette dataset
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── AudioWaveVisualizer.kt      # Glowing audio wave visualizer
│   │   │   │   │   │   ├── ConversationItemView.kt     # Translation bubble with phonetics
│   │   │   │   │   │   ├── DietaryAllergyDialog.kt     # Fullscreen allergy emergency card
│   │   │   │   │   │   └── LanguageSelectorDialog.kt   # Searchable language selector
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── LiveDualTalkScreen.kt       # Dual face-to-face interpreter screen
│   │   │   │   │   │   ├── GeminiLiveScreen.kt         # Gemini 3.8 Live audio orb screen
│   │   │   │   │   │   ├── PhrasebookScreen.kt         # Categorized phrasebook & allergy cards
│   │   │   │   │   │   ├── CultureGuideScreen.kt       # Country etiquette & customs guide
│   │   │   │   │   │   └── SavedPhrasesScreen.kt       # Bookmarked conversations
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt                    # Midnight navy & neon accent palette
│   │   │   │   │   │   ├── Theme.kt                    # Material 3 dynamic & dark themes
│   │   │   │   │   │   └── Type.kt                     # Typography configuration
│   │   │   │   │   └── viewmodel/
│   │   │   │   │       └── VoxTravelViewModel.kt       # Main StateFlow view model
│   │   │   └── res/
│   │   │       ├── drawable/                           # App icon and hero banner visual assets
│   │   │       ├── mipmap-*/                           # Adaptive PNG launcher icons across all densities
│   │   │       └── values/strings.xml                  # App strings ("VoxTravel")
│   │   └── test/java/com/example/
│   │       └── ExampleRobolectricTest.kt              # Robolectric tests for app & Gemini Live
└── metadata.json                              # AI Studio application metadata
```

---

## 🚀 Setup & Execution

### 1. API Key Configuration
1. Open the **Secrets panel in Google AI Studio**.
2. Add your key with the variable name `GEMINI_API_KEY`.
3. The build system injects this into `BuildConfig.GEMINI_API_KEY` at compile time via the Secrets Gradle Plugin.

### 2. Required Permissions
Declared in `AndroidManifest.xml`:
- `android.permission.INTERNET`: For Gemini Live API and REST communications.
- `android.permission.RECORD_AUDIO`: Runtime permission for microphone audio input.
- `android.permission.MODIFY_AUDIO_SETTINGS`: For adjusting speech audio playback volume and routing.

### 3. Running Unit Tests
To run local JVM Robolectric unit tests:
```bash
gradle :app:testDebugUnitTest
```

---
