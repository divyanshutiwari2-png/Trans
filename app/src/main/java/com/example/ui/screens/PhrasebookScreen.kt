package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TravelScenario
import com.example.data.repository.TravelDataRepository
import com.example.ui.theme.AmberTravel
import com.example.ui.theme.CoralAccent
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
fun PhrasebookScreen(
    state: VoxTravelUiState,
    viewModel: VoxTravelViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(TravelScenario.FOOD_DINING) }
    var searchQuery by remember { mutableStateOf("") }

    val phrases = remember(selectedCategory, state.localLang) {
        TravelDataRepository.getPhrasesForScenario(selectedCategory, state.localLang)
    }

    val filteredPhrases = remember(phrases, searchQuery) {
        if (searchQuery.isBlank()) phrases else {
            phrases.filter {
                it.english.contains(searchQuery, ignoreCase = true) ||
                    it.translation.contains(searchQuery, ignoreCase = true) ||
                    it.phonetic.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDeep)
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MidnightNavy,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Travel Phrasebook & Allergies",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Instant phrases in ${state.localLang.flag} ${state.localLang.displayName}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search allergies, food, directions...", color = TextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = TextMuted)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phrasebook_search_input"),
                    shape = RoundedCornerShape(14.dp),
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

                Spacer(modifier = Modifier.height(12.dp))

                // Scenario filter tabs
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(TravelScenario.values()) { sc ->
                        val isSelected = sc == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = sc },
                            label = {
                                Text(
                                    text = "${sc.icon} ${sc.title}",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (sc == TravelScenario.EMERGENCY) CoralAccent.copy(alpha = 0.25f) else CyanNeon.copy(alpha = 0.25f),
                                selectedLabelColor = if (sc == TravelScenario.EMERGENCY) CoralAccent else CyanNeon,
                                containerColor = MidnightSurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (isSelected) {
                                    if (sc == TravelScenario.EMERGENCY) CoralAccent else CyanNeon
                                } else MidnightBorder,
                                enabled = true,
                                selected = isSelected
                            ),
                            modifier = Modifier.testTag("phrase_category_${sc.name}")
                        )
                    }
                }
            }
        }

        // Phrase list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Allergy Alert Banner if in Dining or Emergency
            if (selectedCategory == TravelScenario.FOOD_DINING || selectedCategory == TravelScenario.EMERGENCY) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, CoralAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                        color = CoralAccent.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(CoralAccent.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Allergy notice",
                                    tint = CoralAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Dietary Allergy Warnings",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CoralAccent
                                    )
                                )
                                Text(
                                    text = "Tap any allergy card to open full-screen large print to hand directly to kitchen staff or waitstaff.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            items(filteredPhrases, key = { it.id }) { phrase ->
                val isUrgent = phrase.isEmergency || phrase.dietaryAlert
                val cardBorderColor = if (isUrgent) CoralAccent else MidnightBorder

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, cardBorderColor.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                        .testTag("phrase_card_${phrase.id}"),
                    color = MidnightCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Badge row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isUrgent) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Alert",
                                        tint = CoralAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (phrase.dietaryAlert) "DIETARY ALLERGY" else "EMERGENCY",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = CoralAccent,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            } else {
                                Text(
                                    text = "${selectedCategory.icon} ${selectedCategory.title}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyanNeon,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Fullscreen show card button (especially for allergies)
                                if (isUrgent) {
                                    IconButton(
                                        onClick = { viewModel.showFullscreenPhrase(phrase) },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .testTag("show_fullscreen_${phrase.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Fullscreen,
                                            contentDescription = "Show Large Card",
                                            tint = CoralAccent,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                // Speak button
                                IconButton(
                                    onClick = { viewModel.speakText(phrase.translation, state.localLang) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("speak_phrase_${phrase.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Speak phrase",
                                        tint = if (isUrgent) CoralAccent else CyanNeon,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Target Native Translation
                        Text(
                            text = phrase.translation,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                lineHeight = 24.sp
                            )
                        )

                        // Phonetic if present
                        if (phrase.phonetic.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "🗣️ ${phrase.phonetic}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AmberTravel,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // English meaning
                        Text(
                            text = phrase.english,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    }
}
