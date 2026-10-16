package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioWaveVisualizer(
    isActive: Boolean,
    activeColor: Color,
    volumeRms: Float = 0f,
    barCount: Int = 9,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "wave")
    val animProgress by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_anim"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val baseMultipliers = listOf(0.4f, 0.7f, 1.0f, 0.85f, 0.5f, 0.9f, 1.0f, 0.6f, 0.3f)

        for (i in 0 until barCount) {
            val mult = baseMultipliers.getOrElse(i % baseMultipliers.size) { 0.5f }
            val heightRatio = if (isActive) {
                val waveFactor = ((animProgress + mult) % 1.0f)
                val dynamicVolume = volumeRms.coerceIn(0.1f, 1.0f)
                ((waveFactor * 0.7f + dynamicVolume * 0.3f) * 36.dp.value).coerceIn(6f, 40f)
            } else {
                4f
            }

            Box(
                modifier = Modifier
                    .width(3.5.dp)
                    .height(heightRatio.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        if (isActive) {
                            Brush.verticalGradient(
                                listOf(
                                    activeColor,
                                    activeColor.copy(alpha = 0.6f)
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.25f),
                                    Color.White.copy(alpha = 0.1f)
                                )
                            )
                        }
                    )
            )
        }
    }
}
