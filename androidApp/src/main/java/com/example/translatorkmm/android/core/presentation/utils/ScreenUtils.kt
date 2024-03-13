package com.example.translatorkmm.android.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ScreenInfo(
    val screenWidthInfo: ScreenType,
    val screenHeightInfo: ScreenType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class ScreenType {
        data object Compact: ScreenType()
        data object Medium: ScreenType()
        data object Expanded: ScreenType()
    }
}

@Composable
fun rememberScreenInfo(): ScreenInfo {
    val config = LocalConfiguration.current
    return ScreenInfo(
        screenWidthInfo = getScreenWidthType(config.screenWidthDp),
        screenHeightInfo = getScreenHeightType(config.screenHeightDp),
        screenWidth = config.screenWidthDp.dp,
        screenHeight = config.screenHeightDp.dp
    )
}

private fun getScreenWidthType(width: Int): ScreenInfo.ScreenType = when {
    width < 600 -> ScreenInfo.ScreenType.Compact
    width < 840 -> ScreenInfo.ScreenType.Medium
    else -> ScreenInfo.ScreenType.Expanded
}

private fun getScreenHeightType(height: Int): ScreenInfo.ScreenType = when {
    height < 480 -> ScreenInfo.ScreenType.Compact
    height < 900 -> ScreenInfo.ScreenType.Medium
    else -> ScreenInfo.ScreenType.Expanded
}