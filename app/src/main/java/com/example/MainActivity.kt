package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.SpeakerSide
import com.example.ui.components.DietaryAllergyDialog
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.screens.CultureGuideScreen
import com.example.ui.screens.GeminiLiveScreen
import com.example.ui.screens.LiveDualTalkScreen
import com.example.ui.screens.PhrasebookScreen
import com.example.ui.screens.SavedPhrasesScreen
import com.example.ui.theme.AmberTravel
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightDeep
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.VoxTravelViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: VoxTravelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: VoxTravelViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isSettingsOpen by remember { mutableStateOf(false) }

    // Request audio record permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // gracefully handled: user can still type or tap phrases
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        containerColor = MidnightDeep,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MidnightNavy,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // App Logo & Gemini badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CyanNeon),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✈️", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "VoxTravel",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CyanNeon.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "Gemini",
                                            tint = CyanNeon,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "Gemini AI",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = CyanNeon,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Live Voice Translation & Nuance",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    // Settings Dialog button
                    IconButton(
                        onClick = { isSettingsOpen = true },
                        modifier = Modifier.testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Audio & Travel Settings",
                            tint = TextSecondary
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = MidnightNavy,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = state.activeTab == AppTab.LIVE_TALK,
                    onClick = { viewModel.selectTab(AppTab.LIVE_TALK) },
                    icon = { Icon(Icons.Default.Mic, contentDescription = "Live Talk") },
                    label = { Text("Live Talk", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightDeep,
                        selectedTextColor = CyanNeon,
                        indicatorColor = CyanNeon,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_live_talk")
                )

                NavigationBarItem(
                    selected = state.activeTab == AppTab.GEMINI_LIVE,
                    onClick = { viewModel.selectTab(AppTab.GEMINI_LIVE) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Live AI") },
                    label = { Text("Live AI", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightDeep,
                        selectedTextColor = CyanNeon,
                        indicatorColor = CyanNeon,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_gemini_live")
                )

                NavigationBarItem(
                    selected = state.activeTab == AppTab.PHRASEBOOK,
                    onClick = { viewModel.selectTab(AppTab.PHRASEBOOK) },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Phrases") },
                    label = { Text("Phrases", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightDeep,
                        selectedTextColor = CyanNeon,
                        indicatorColor = CyanNeon,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_phrasebook")
                )

                NavigationBarItem(
                    selected = state.activeTab == AppTab.CULTURE,
                    onClick = { viewModel.selectTab(AppTab.CULTURE) },
                    icon = { Icon(Icons.Default.Public, contentDescription = "Culture") },
                    label = { Text("Culture", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightDeep,
                        selectedTextColor = AmberTravel,
                        indicatorColor = AmberTravel,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_culture")
                )

                NavigationBarItem(
                    selected = state.activeTab == AppTab.SAVED,
                    onClick = { viewModel.selectTab(AppTab.SAVED) },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") },
                    label = { Text("Saved", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightDeep,
                        selectedTextColor = CyanNeon,
                        indicatorColor = CyanNeon,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_saved")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.activeTab) {
                AppTab.LIVE_TALK -> LiveDualTalkScreen(state = state, viewModel = viewModel)
                AppTab.GEMINI_LIVE -> GeminiLiveScreen(state = state, viewModel = viewModel)
                AppTab.PHRASEBOOK -> PhrasebookScreen(state = state, viewModel = viewModel)
                AppTab.CULTURE -> CultureGuideScreen(state = state, viewModel = viewModel)
                AppTab.SAVED -> SavedPhrasesScreen(state = state, viewModel = viewModel)
            }
        }
    }

    // Modal: Language selector
    if (state.isLanguageDialogVisible) {
        val currentSelected = if (state.languageDialogTarget == SpeakerSide.TRAVELER) state.travelerLang else state.localLang
        LanguageSelectorDialog(
            targetSide = state.languageDialogTarget,
            currentSelected = currentSelected,
            onSelect = { lang -> viewModel.selectLanguage(lang) },
            onDismiss = { viewModel.closeLanguagePicker() }
        )
    }

    // Modal: Fullscreen Allergy Card
    state.activeFullscreenPhrase?.let { phrase ->
        DietaryAllergyDialog(
            phrase = phrase,
            onSpeak = { text -> viewModel.speakText(text, state.localLang) },
            onDismiss = { viewModel.showFullscreenPhrase(null) }
        )
    }

    // Audio & Travel Settings Bottom Sheet
    if (isSettingsOpen) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { isSettingsOpen = false },
            sheetState = sheetState,
            containerColor = MidnightCard,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(TextMuted)
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Audio & Travel Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Auto-speak toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Auto-Speak Audio Translations",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Audibly speaks the translated response aloud through speaker",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                    Switch(
                        checked = state.autoSpeakEnabled,
                        onCheckedChange = { viewModel.toggleAutoSpeak() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MidnightDeep,
                            checkedTrackColor = CyanNeon
                        ),
                        modifier = Modifier.testTag("settings_autospeak_switch")
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Speech Speed Slider
                Text(
                    text = "Speech Playback Speed: ${(state.speechRate * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Slower rates (0.7x - 0.9x) help foreign listeners understand clearly in noisy train stations and markets",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Slider(
                    value = state.speechRate,
                    onValueChange = { viewModel.setSpeechRate(it) },
                    valueRange = 0.6f..1.3f,
                    steps = 6,
                    colors = SliderDefaults.colors(
                        thumbColor = CyanNeon,
                        activeTrackColor = CyanNeon,
                        inactiveTrackColor = MidnightSurface
                    ),
                    modifier = Modifier.testTag("speech_rate_slider")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Table Flip View
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Face-to-Face Table Flip",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Flips the local conversation turns 180° so someone sitting opposite at a cafe or counter can read naturally",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                    Switch(
                        checked = state.isDualViewFlipped,
                        onCheckedChange = { viewModel.toggleDualViewFlip() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MidnightDeep,
                            checkedTrackColor = AmberTravel
                        ),
                        modifier = Modifier.testTag("settings_table_flip_switch")
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
