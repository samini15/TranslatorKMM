package com.example.translatorkmm.android.translate.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.translatorkmm.R
import com.example.translatorkmm.android.core.theme.LightBlue
import com.example.translatorkmm.android.core.theme.LocalSpacing
import com.example.translatorkmm.core.domain.language.Language
import com.example.translatorkmm.core.presentation.UiLanguage

@Composable
fun LanguageDisplay(
    modifier: Modifier = Modifier,
    language: UiLanguage
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallLanguageIcon(language = language)

        Spacer(modifier = modifier.width(spacing.spaceSmall))

        Text(text = language.language.langName, color = LightBlue)
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageDisplayPreview() {
    val language = UiLanguage(R.drawable.persian, Language.byCode("fa"))
    LanguageDisplay(modifier = Modifier, language = language)
}