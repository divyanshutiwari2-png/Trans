package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.GeminiLiveService
import com.example.data.model.Language
import com.example.data.model.TravelScenario
import com.example.data.repository.TravelDataRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("VoxTravel", appName)
  }

  @Test
  fun `verify essential travel phrasebook phrases loaded`() {
    val emergencyPhrases = TravelDataRepository.getPhrasesForScenario(
      TravelScenario.EMERGENCY,
      Language.DEFAULT_LOCAL
    )
    assertTrue("Should have emergency phrases", emergencyPhrases.isNotEmpty())
    val allergyPhrase = emergencyPhrases.firstOrNull { it.dietaryAlert }
    assertNotNull("Should contain dietary allergy alert phrase", allergyPhrase)
    assertTrue("Should be marked emergency", allergyPhrase!!.isEmergency)
  }

  @Test
  fun `verify country etiquette data exists`() {
    val japanEtiquette = TravelDataRepository.getEtiquetteForLanguage("ja")
    assertEquals("Japan", japanEtiquette.country)
    assertTrue(japanEtiquette.diningRules.isNotEmpty())
  }

  @Test
  fun `verify gemini 3 8 live model configuration and voices`() {
    assertEquals("gemini-3.8-live", GeminiLiveService.LIVE_MODEL)
    val voices = GeminiLiveService.AVAILABLE_VOICES
    assertTrue(voices.contains("Aoede"))
    assertTrue(voices.contains("Kore"))
    assertTrue(voices.contains("Fenrir"))
    assertTrue(voices.contains("Puck"))
    assertTrue(voices.contains("Charon"))
  }
}
