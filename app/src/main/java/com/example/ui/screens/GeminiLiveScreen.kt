package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.GeminiLiveService
import com.example.data.api.LiveConnectionState
import com.example.ui.components.AudioWaveVisualizer
import com.example.ui.theme.AmberTravel
import com.example.ui.theme.AzureGlow
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldMint
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
fun GeminiLiveScreen(
    state: VoxTravelUiState,
    viewModel: VoxTravelViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var manualInputText by remember { mutableStateOf("") }

    // Auto-scroll on new messages or streaming chunks
    LaunchedEffect(state.liveChatMessages.size, state.liveCurrentStreamChunk) {
        if (state.liveChatMessages.isNotEmpty() || state.liveCurrentStreamChunk.isNotEmpty()) {
            val count = state.liveChatMessages.size + if (state.liveCurrentStreamChunk.isNotEmpty()) 1 else 0
            if (count > 0) {
                listState.animateScrollToItem(count - 1)
            }
        }
    }

    // Orb animation transitions
    val transition = rememberInfiniteTransition(label = "orb_pulse")
    val pulseScale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val rotateDegrees by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate_degrees"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDeep)
    ) {
        // --- Top Header with Live Model Status ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MidnightNavy,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Gemini Live Voice",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // Model badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyanNeon.copy(alpha = 0.2f))
                                    .border(1.dp, CyanNeon.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "gemini-3.8-live",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyanNeon,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Real-time bidirectional spoken conversation",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    // Connection indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MidnightCard)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        val isConnected = state.liveConnectionState == LiveConnectionState.CONNECTED ||
                            state.liveConnectionState == LiveConnectionState.STREAMING_RESPONSE
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isConnected) EmeraldMint else AmberTravel)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isConnected) "Live Active" else "Ready",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isConnected) EmeraldMint else AmberTravel,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Voice persona and hands-free toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Voice Persona Selection
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(GeminiLiveService.AVAILABLE_VOICES) { voice ->
                            val isSelected = voice == state.liveActiveVoice
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setLiveVoice(voice) },
                                label = {
                                    Text(
                                        text = voice,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanNeon.copy(alpha = 0.25f),
                                    selectedLabelColor = CyanNeon,
                                    containerColor = MidnightSurface,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (isSelected) CyanNeon else MidnightBorder,
                                    enabled = true,
                                    selected = isSelected
                                ),
                                modifier = Modifier.testTag("voice_persona_$voice")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Clear Chat
                    if (state.liveChatMessages.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearLiveChat() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- Center Animated Gemini Live Orb ---
        val isOrbActive = state.isListening || state.isSpeakingTts ||
            state.liveConnectionState == LiveConnectionState.STREAMING_RESPONSE
        val dynamicScale = if (isOrbActive) {
            pulseScale * (1f + state.audioVolumeRms * 0.35f)
        } else {
            1f
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outermost glowing ripple ring
            if (isOrbActive) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(dynamicScale * 1.15f)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    CyanNeon.copy(alpha = 0.22f),
                                    AzureGlow.copy(alpha = 0.10f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            // Main Iridescent Live Orb
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .scale(dynamicScale)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(
                                CyanNeon,
                                AzureGlow,
                                Color(0xFF8B5CF6), // Royal violet
                                CoralAccent,
                                AmberTravel,
                                CyanNeon
                            )
                        )
                    )
                    .clickable { viewModel.toggleLiveMic() }
                    .testTag("gemini_live_orb"),
                contentAlignment = Alignment.Center
            ) {
                // Inner dark glow center
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    MidnightNavy,
                                    MidnightDeep
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isListening) {
                        AudioWaveVisualizer(
                            isActive = true,
                            activeColor = CyanNeon,
                            volumeRms = state.audioVolumeRms,
                            barCount = 7
                        )
                    } else if (state.isSpeakingTts || state.liveConnectionState == LiveConnectionState.STREAMING_RESPONSE) {
                        AudioWaveVisualizer(
                            isActive = true,
                            activeColor = AmberTravel,
                            volumeRms = 0.7f,
                            barCount = 7
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Gemini Live",
                            tint = CyanNeon,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // Subtitle status
        Text(
            text = when {
                state.isListening -> "Listening to your voice..."
                state.isSpeakingTts -> "Gemini is speaking with ${state.liveActiveVoice}..."
                state.liveConnectionState == LiveConnectionState.STREAMING_RESPONSE -> "Streaming response from gemini-3.8-live..."
                else -> "Tap the orb or mic to talk in real-time"
            },
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isOrbActive) CyanNeon else TextSecondary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // --- Quick Travel Live Conversation Prompts ---
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = listOf(
                "🍜 Act as my live interpreter for ordering food",
                "🍣 Practice ordering sushi in Japanese with me",
                "🚇 How do I ride the subway here politely?",
                "🚕 What should I say to a taxi driver?",
                "🍶 What is the custom for pouring drinks?"
            )
            items(suggestions) { prompt ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, MidnightBorder, RoundedCornerShape(12.dp))
                        .clickable { viewModel.sendLiveMessage(prompt.substring(2).trim()) }
                        .testTag("quick_prompt_${prompt.take(6)}"),
                    color = MidnightCard
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextPrimary,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Live Transcript Message Stream ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.liveChatMessages, key = { it.id }) { msg ->
                    LiveChatBubble(message = msg, onSpeak = {
                        viewModel.speakText(msg.text, state.travelerLang)
                    })
                }

                // Currently streaming chunk in progress
                if (state.liveCurrentStreamChunk.isNotBlank()) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, CyanNeon.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                            color = MidnightCard
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI",
                                    tint = CyanNeon,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = state.liveCurrentStreamChunk,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = TextPrimary,
                                            lineHeight = 22.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "⚡ Streaming live from gemini-3.8-live...",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = CyanNeon,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Bottom Live Audio Console ---
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
                // Hands-Free Loop Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Hands-Free",
                            tint = if (state.isHandsFreeLiveLoop) EmeraldMint else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Continuous Hands-Free Conversation",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (state.isHandsFreeLiveLoop) TextPrimary else TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Switch(
                        checked = state.isHandsFreeLiveLoop,
                        onCheckedChange = { viewModel.toggleHandsFreeLiveLoop() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MidnightDeep,
                            checkedTrackColor = EmeraldMint
                        ),
                        modifier = Modifier.testTag("hands_free_switch")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Mic and Text input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Big Live Talking Mic Trigger Button
                    Surface(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (state.isListening) 2.dp else 1.dp,
                                color = if (state.isListening) CyanNeon else CyanNeon.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable { viewModel.toggleLiveMic() }
                            .testTag("live_mic_action_button"),
                        color = if (state.isListening) CyanNeon.copy(alpha = 0.25f) else MidnightCard
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (state.isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Live Mic",
                                tint = CyanNeon,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = manualInputText,
                        onValueChange = { manualInputText = it },
                        placeholder = { Text("Ask or say anything to Gemini Live...", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("live_manual_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MidnightSurface,
                            unfocusedContainerColor = MidnightSurface,
                            focusedBorderColor = CyanNeon,
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
                                viewModel.sendLiveMessage(manualInputText)
                                manualInputText = ""
                            }
                        },
                        enabled = manualInputText.isNotBlank(),
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (manualInputText.isNotBlank()) CyanNeon else MidnightSurface)
                            .testTag("send_live_text_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (manualInputText.isNotBlank()) MidnightDeep else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveChatBubble(
    message: com.example.data.api.LiveChatMessage,
    onSpeak: () -> Unit
) {
    val isUser = message.isUser
    val align = if (isUser) Alignment.End else Alignment.Start
    val bg = if (isUser) AzureGlow.copy(alpha = 0.25f) else MidnightCard
    val borderColor = if (isUser) AzureGlow.copy(alpha = 0.6f) else MidnightBorder

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, borderColor, RoundedCornerShape(18.dp)),
            color = bg
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "You (Traveler)" else "Gemini Live",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isUser) AzureGlow else CyanNeon,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    if (!isUser) {
                        IconButton(
                            onClick = onSpeak,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Speak",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                )
            }
        }
    }
}
