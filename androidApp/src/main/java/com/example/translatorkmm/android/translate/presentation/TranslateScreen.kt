package com.example.translatorkmm.android.translate.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.translatorkmm.android.core.theme.LocalSpacing
import com.example.translatorkmm.android.translate.presentation.components.LanguageDropDownMenu
import com.example.translatorkmm.android.translate.presentation.components.SwapLanguagesButton
import com.example.translatorkmm.translate.presentation.TranslateEvent
import com.example.translatorkmm.translate.presentation.TranslateState

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit
) {
    val spacing = LocalSpacing.current
    Scaffold(
        floatingActionButton = {

        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
        ) {
            item {
                // Language selection Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // From Language
                    LanguageDropDownMenu(
                        language = state.fromLanguage,
                        isOpen = state.isChoosingFromLanguage,
                        onClick = { onEvent(TranslateEvent.OpenFromLanguageDropDown) },
                        onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
                        onSelectedLanguage = { onEvent(TranslateEvent.ChooseFromLanguage(it))}
                    )

                    //Spacer(modifier = Modifier.weight(1f))

                    SwapLanguagesButton(onClick = { onEvent(TranslateEvent.SwapLanguages) })

                    //Spacer(modifier = Modifier.weight(1f))

                    // Target Language
                    LanguageDropDownMenu(
                        language = state.toLanguage,
                        isOpen = state.isChoosingToLanguage,
                        onClick = { onEvent(TranslateEvent.OpenToLanguageDropDown) },
                        onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
                        onSelectedLanguage = { onEvent(TranslateEvent.ChooseToLanguage(it))}
                    )
                }
            }
        }
    }
}