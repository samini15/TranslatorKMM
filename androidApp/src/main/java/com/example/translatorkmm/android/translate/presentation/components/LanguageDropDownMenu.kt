package com.example.translatorkmm.android.translate.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.translatorkmm.android.core.theme.LightBlue
import com.example.translatorkmm.android.core.theme.LocalSpacing
import com.example.translatorkmm.core.domain.language.Language
import com.example.translatorkmm.core.presentation.UiLanguage

@Composable
fun LanguageDropDownMenu(
    modifier: Modifier = Modifier,
    language: UiLanguage,
    isOpen: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onSelectedLanguage: (UiLanguage) -> Unit
) {
    val spacing = LocalSpacing.current
    Box(modifier = modifier) {
        DropdownMenu(expanded = isOpen, onDismissRequest = onDismiss) {
            UiLanguage.allLanguages.forEach {  language ->
                LanguageDropDownItem(
                    modifier = Modifier.fillMaxWidth(),
                    language = language,
                    onClick = { onSelectedLanguage(language) }
                )
            }
        }
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(spacing.spaceSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(spacing.spaceLarge),
                model = language.drawableRes,
                contentDescription = language.language.langName
            )

            Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))

            Text(text = language.language.langName, color = LightBlue)

            Icon(
                modifier = Modifier.size(spacing.spaceLarge),
                imageVector = if (isOpen) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                tint = LightBlue,
                contentDescription = ""
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageDropDownMenuPreview() {
    val language = UiLanguage(com.example.translatorkmm.R.drawable.persian, Language.byCode("fa"))
    LanguageDropDownMenu(modifier = Modifier, language = language, isOpen = true, onClick = {}, onDismiss = {}, onSelectedLanguage = {})
}