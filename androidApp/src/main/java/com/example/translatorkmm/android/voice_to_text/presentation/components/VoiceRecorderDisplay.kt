package com.example.translatorkmm.android.voice_to_text.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatorkmm.android.core.theme.LocalSpacing
import com.example.translatorkmm.android.core.theme.TranslatorKMMTheme
import com.example.translatorkmm.android.translate.presentation.components.gradientSurface
import kotlin.random.Random

@Composable
fun VoiceRecorderDisplay(
    modifier: Modifier = Modifier,
    powerRatios: List<Float>
) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .gradientSurface()
            .padding(
                horizontal = spacing.spaceLarge,
                vertical = spacing.spaceSmall
            )
            .drawBehind {
                val powerRatioWidth = 3.dp.toPx()
                val powerRatioCount = (size.width / (2 * powerRatioWidth)).toInt()

                clipRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height
                ) {
                    powerRatios
                        .takeLast(powerRatioCount)
                        .reversed()
                        .forEachIndexed { index, ratio ->
                            val yTopStart = center.y - (size.height / 2f) * ratio

                            drawRoundRect(
                                color = Color.Blue,
                                topLeft = Offset(
                                    x = size.width - index * 2 * powerRatioWidth,
                                    y = yTopStart
                                ),
                                size = Size(
                                    width = powerRatioWidth,
                                    height = (center.y - yTopStart) * 2f
                                ),
                                cornerRadius = CornerRadius(100f)
                            )
                        }
                }
            }
    ) {

    }
}

@Preview
@Composable
private fun VoiceRecorderDisplayPreview() {
    TranslatorKMMTheme {
        VoiceRecorderDisplay(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            powerRatios = (0..100).map {
                Random.nextFloat()
            }
        )
    }
}