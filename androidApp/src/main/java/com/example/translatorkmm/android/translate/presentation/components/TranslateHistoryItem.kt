package com.example.translatorkmm.android.translate.presentation.components

import android.content.res.Configuration
import android.view.Display.Mode
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatorkmm.R
import com.example.translatorkmm.android.core.theme.LightBlue
import com.example.translatorkmm.android.core.theme.LocalSpacing
import com.example.translatorkmm.core.domain.language.Language
import com.example.translatorkmm.core.presentation.UiLanguage
import com.example.translatorkmm.translate.presentation.UiHistoryItem

@Composable
fun TranslateHistoryItem(
    modifier: Modifier = Modifier,
    item: UiHistoryItem,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .shadow(elevation = 5.dp)
            .clip(RoundedCornerShape(20.dp))
            .gradientSurface()
            .clickable(onClick = onClick)
            .padding(spacing.spaceMedium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallLanguageIcon(language = item.fromLanguage)

            Spacer(modifier = Modifier.width(spacing.spaceMedium))

            Text(
                text = item.fromText,
                color = LightBlue,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(spacing.spaceLarge))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallLanguageIcon(language = item.toLanguage)

            Spacer(modifier = Modifier.width(spacing.spaceMedium))

            Text(
                text = item.toText,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TranslateHistoryItemPreview() {
    val fromLanguage = UiLanguage(R.drawable.french, Language.byCode("fr"))
    val toLanguage = UiLanguage(R.drawable.persian, Language.byCode("fa"))
    val historyItem = UiHistoryItem(id = 0, fromLanguage = fromLanguage, fromText = "Bonjour", toLanguage = toLanguage, toText = "a")
    TranslateHistoryItem(item = historyItem) {

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TranslateHistoryItemPreviewDark() {
    val fromLanguage = UiLanguage(R.drawable.french, Language.byCode("fr"))
    val toLanguage = UiLanguage(R.drawable.persian, Language.byCode("fa"))
    val historyItem = UiHistoryItem(id = 0, fromLanguage = fromLanguage, fromText = "Bonjour", toLanguage = toLanguage, toText = "a")
    TranslateHistoryItem(item = historyItem) {

    }
}