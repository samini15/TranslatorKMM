package com.example.translatorkmm.android.translate.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatorkmm.R.drawable
import com.example.translatorkmm.android.core.theme.LocalSpacing
import com.example.translatorkmm.core.domain.language.Language
import com.example.translatorkmm.core.presentation.UiLanguage

@Composable
fun LanguageDropDownItem(
    modifier: Modifier = Modifier,
    language: UiLanguage,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    DropdownMenuItem(modifier = modifier, onClick = onClick) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = language.drawableRes),
            contentDescription = language.language.langName
        )

        Spacer(modifier = Modifier.width(spacing.spaceMedium))
        
        Text(text = language.language.langName.lowercase().capitalize(Locale.current))
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageDropDownItemPreview() {
    val language = UiLanguage(drawable.persian, Language.byCode("fa"))
    LanguageDropDownItem(modifier = Modifier, language = language) { }
}