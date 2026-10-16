package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SpeakerSide
import com.example.data.model.TravelScenario
import com.example.ui.components.AudioWaveVisualizer
import com.example.ui.components.ConversationItemView
import com.example.ui.theme.AmberTravel
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.MidnightBorder
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightDeep
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.VoxTravelUiState
import com.example.ui.viewmodel.VoxTravelViewModel

@Composable
fun LiveDualTalkScreen(
    state: VoxTravelUiState,
    viewModel: VoxTravelViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var manualInputText by remember { mutableStateOf("") }
    var selectedTypingSpeaker by remember { mutableStateOf(SpeakerSide.TRAVELER) }

    // Scroll to latest turn when conversation updates
    LaunchedEffect(state.conversations.size) {
        if (state.conversations.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDeep)
    ) {
        // --- Top Language & Scenario Control Bar ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MidnightNavy,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Dual Language Selector Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Traveler Language Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, CyanNeon.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .clickable { viewModel.openLanguagePicker(SpeakerSide.TRAVELER) }
                            .testTag("traveler_language_selector"),
                        color = MidnightCard
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = state.travelerLang.flag, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Traveler",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyanNeon,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = state.travelerLang.displayName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Swap Button
                    IconButton(
                        onClick = { viewModel.swapLanguages() },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .testTag("swap_languages_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Swap Languages",
                            tint = CyanNeon,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Local Destination Language Button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, AmberTravel.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .clickable { viewModel.openLanguagePicker(SpeakerSide.LOCAL) }
                            .testTag("local_language_selector"),
                        color = MidnightCard
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = state.localLang.flag, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Local Destination",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AmberTravel,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = state.localLang.displayName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scenario Pills & Quick Toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(TravelScenario.values()) { sc ->
                            val isSelected = sc == state.activeScenario
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setScenario(sc) },
                                label = {
                                    Text(
                                        text = "${sc.icon} ${sc.title}",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanNeon.copy(alpha = 0.2f),
                                    selectedLabelColor = CyanNeon,
                                    containerColor = MidnightSurface,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (isSelected) CyanNeon else MidnightBorder,
                                    enabled = true,
                                    selected = isSelected
                                ),
                                modifier = Modifier.testTag("scenario_chip_${sc.name}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Auto-Speak Audio Toggle
                    IconButton(
                        onClick = { viewModel.toggleAutoSpeak() },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("toggle_autospeak_button")
                    ) {
                        Icon(
                            imageVector = if (state.autoSpeakEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Auto Speak",
                            tint = if (state.autoSpeakEnabled) CyanNeon else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Table Flip View (180° rotation for face-to-face table mode)
                    IconButton(
                        onClick = { viewModel.toggleDualViewFlip() },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("toggle_table_flip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ScreenRotation,
                            contentDescription = "Flip Table View",
                            tint = if (state.isDualViewFlipped) AmberTravel else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Clear conversation
                    if (state.conversations.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearHistory() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("clear_conversation_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear History",
                                tint = TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Status banner if error or message
        AnimatedVisibility(visible = state.statusMessage != null) {
            state.statusMessage?.let { msg ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.dismissStatusMessage() },
                    color = MidnightCard
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ℹ️ $msg",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Dismiss",
                            style = MaterialTheme.typography.labelSmall.copy(color = CyanNeon)
                        )
                    }
                }
            }
        }

        // --- Live Voice Status Overlay (Active when listening or speaking) ---
        AnimatedVisibility(
            visible = state.isListening || state.isSpeakingTts || state.isTranslating,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val isTraveler = state.listeningSpeaker == SpeakerSide.TRAVELER
            val activeColor = if (isTraveler) CyanNeon else AmberTravel

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, activeColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                color = MidnightCard
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AudioWaveVisualizer(
                        isActive = state.isListening || state.isSpeakingTts,
                        activeColor = activeColor,
                        volumeRms = state.audioVolumeRms,
                        barCount = 7
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when {
                                state.isTranslating -> "Gemini interpreting travel nuance..."
                                state.isSpeakingTts -> "Speaking translation out loud..."
                                state.isListening -> "Listening to ${if (isTraveler) "Traveler" else "Local"}..."
                                else -> "Ready"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = activeColor
                            )
                        )
                        if (state.interimSpeechText.isNotBlank()) {
                            Text(
                                text = state.interimSpeechText,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                                maxLines = 1
                            )
                        }
                    }

                    if (state.isTranslating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = activeColor,
                            strokeWidth = 2.dp
                        )
                    } else if (state.isListening) {
                        IconButton(
                            onClick = { viewModel.stopVoiceInput() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MicOff,
                                contentDescription = "Stop Listening",
                                tint = activeColor
                            )
                        }
                    }
                }
            }
        }

        // --- Conversation Turn Stream ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.conversations.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🎙️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Real-Time Travel Talk",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap the Cyan mic for Traveler (${state.travelerLang.displayName})\nor Amber mic for Local (${state.localLang.displayName}).\nAudio translations play automatically!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.conversations, key = { it.id }) { item ->
                        val shouldFlip = state.isDualViewFlipped && item.speaker == SpeakerSide.LOCAL
                        Box(
                            modifier = if (shouldFlip) Modifier.rotate(180f) else Modifier
                        ) {
                            ConversationItemView(
                                item = item,
                                isSpeaking = state.isSpeakingTts,
                                onSpeak = { viewModel.speakText(item.targetText, item.targetLang) },
                                onToggleBookmark = { viewModel.toggleBookmark(item.id) },
                                onQuickReplyClick = { reply, side ->
                                    viewModel.processTranslation(reply, side)
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- Bottom Dual Mic Talking Console ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MidnightNavy,
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Dual Big Mic Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Traveler Talking Button (Left - Cyan)
                    val isTravelerListening = state.isListening && state.listeningSpeaker == SpeakerSide.TRAVELER
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                width = if (isTravelerListening) 2.dp else 1.dp,
                                color = if (isTravelerListening) CyanNeon else CyanNeon.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.startVoiceInput(SpeakerSide.TRAVELER) }
                            .testTag("traveler_voice_button"),
                        color = if (isTravelerListening) CyanNeon.copy(alpha = 0.2f) else MidnightCard
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(CyanNeon, CyanNeon.copy(alpha = 0.7f))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isTravelerListening) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "Speak Traveler",
                                    tint = MidnightDeep,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isTravelerListening) "Listening..." else "Traveler",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CyanNeon
                                    )
                                )
                                Text(
                                    text = state.travelerLang.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary
                                    )
                                )
                            }
                        }
                    }

                    // Local Native Speaker Talking Button (Right - Amber)
                    val isLocalListening = state.isListening && state.listeningSpeaker == SpeakerSide.LOCAL
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                width = if (isLocalListening) 2.dp else 1.dp,
                                color = if (isLocalListening) AmberTravel else AmberTravel.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.startVoiceInput(SpeakerSide.LOCAL) }
                            .testTag("local_voice_button"),
                        color = if (isLocalListening) AmberTravel.copy(alpha = 0.2f) else MidnightCard
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(AmberTravel, AmberTravel.copy(alpha = 0.7f))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isLocalListening) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "Speak Local",
                                    tint = MidnightDeep,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isLocalListening) "Listening..." else "Local Native",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AmberTravel
                                    )
                                )
                                Text(
                                    text = state.localLang.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Silent / Noisy Market Manual Text Input fallback
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Speaker toggle for text input
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MidnightSurface)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedTypingSpeaker == SpeakerSide.TRAVELER) CyanNeon.copy(alpha = 0.3f) else Color.Transparent)
                                .clickable { selectedTypingSpeaker = SpeakerSide.TRAVELER }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = state.travelerLang.flag,
                                fontSize = 14.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedTypingSpeaker == SpeakerSide.LOCAL) AmberTravel.copy(alpha = 0.3f) else Color.Transparent)
                                .clickable { selectedTypingSpeaker = SpeakerSide.LOCAL }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = state.localLang.flag,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = manualInputText,
                        onValueChange = { manualInputText = it },
                        placeholder = {
                            Text(
                                text = "Type in ${if (selectedTypingSpeaker == SpeakerSide.TRAVELER) state.travelerLang.displayName else state.localLang.displayName}...",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("manual_translation_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MidnightSurface,
                            unfocusedContainerColor = MidnightSurface,
                            focusedBorderColor = if (selectedTypingSpeaker == SpeakerSide.TRAVELER) CyanNeon else AmberTravel,
                            unfocusedBorderColor = MidnightBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (manualInputText.isNotBlank()) {
                                viewModel.processTranslation(manualInputText, selectedTypingSpeaker)
                                manualInputText = ""
                            }
                        },
                        enabled = manualInputText.isNotBlank(),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (manualInputText.isNotBlank()) {
                                    if (selectedTypingSpeaker == SpeakerSide.TRAVELER) CyanNeon else AmberTravel
                                } else {
                                    MidnightSurface
                                }
                            )
                            .testTag("send_manual_translation_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send translation",
                            tint = if (manualInputText.isNotBlank()) MidnightDeep else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
