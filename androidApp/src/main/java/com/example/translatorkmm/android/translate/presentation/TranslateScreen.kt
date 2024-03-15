package com.example.translatorkmm.android.translate.presentation

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.example.translatorkmm.android.R
import com.example.translatorkmm.android.core.presentation.utils.ScreenInfo
import com.example.translatorkmm.android.core.theme.LocalSpacing
import com.example.translatorkmm.android.translate.presentation.components.LanguageDropDownMenu
import com.example.translatorkmm.android.translate.presentation.components.SwapLanguagesButton
import com.example.translatorkmm.android.translate.presentation.components.TranslateHistoryItem
import com.example.translatorkmm.android.translate.presentation.components.TranslateTextField
import com.example.translatorkmm.android.translate.presentation.components.rememberTextToSpeech
import com.example.translatorkmm.translate.domain.translate.TranslateError
import com.example.translatorkmm.translate.presentation.TranslateEvent
import com.example.translatorkmm.translate.presentation.TranslateState

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
    screenInfo: ScreenInfo
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    
    LaunchedEffect(key1 = state.error) {
        val message = when (state.error) {
            TranslateError.SERVICE_UNAVAILABLE -> context.getString(R.string.error_service_unavailable)
            TranslateError.CLIENT_ERROR -> context.getString(R.string.client_error)
            TranslateError.SERVER_ERROR -> context.getString(R.string.server_error)
            TranslateError.UNKNOWN_ERROR -> context.getString(R.string.unknown_error)
            else -> null
        }
        message?.let {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            onEvent(TranslateEvent.OnErrorSeen)
        }
    }
    
    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                modifier = Modifier
                    .size(75.dp),
                onClick = {
                    onEvent(TranslateEvent.RecordAudio)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.mic),
                    contentDescription = stringResource(id = R.string.record_audio)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
        ) {
            // region Language Choice
            item {
                if (screenInfo.screenWidthInfo is ScreenInfo.ScreenType.Compact) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // From Language
                        LanguageDropDownMenu(
                            language = state.fromLanguage,
                            isOpen = state.isChoosingFromLanguage,
                            onClick = { onEvent(TranslateEvent.OpenFromLanguageDropDown) },
                            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
                            onSelectedLanguage = { onEvent(TranslateEvent.ChooseFromLanguage(it))}
                        )

                        SwapLanguagesButton(onClick = { onEvent(TranslateEvent.SwapLanguages) })

                        // Target Language
                        LanguageDropDownMenu(
                            language = state.toLanguage,
                            isOpen = state.isChoosingToLanguage,
                            onClick = { onEvent(TranslateEvent.OpenToLanguageDropDown) },
                            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
                            onSelectedLanguage = { onEvent(TranslateEvent.ChooseToLanguage(it))}
                        )
                    }
                } else {
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
            // endregion Language Choice

            // region Translation Field
            item {
                val clipBoardManager = LocalClipboardManager.current
                val keyboardController = LocalSoftwareKeyboardController.current
                val textToSpeech = rememberTextToSpeech()
                TranslateTextField(
                    modifier = Modifier.fillMaxWidth(),
                    fromLanguage = state.fromLanguage,
                    fromText = state.fromText,
                    toLanguage = state.toLanguage,
                    toText = state.toText,
                    isTranslating = state.isTranslating,
                    onTextChanged = {
                        onEvent(TranslateEvent.ChangeTranslationText(it))
                    },
                    onCopyClick = { text ->
                        clipBoardManager.setText(buildAnnotatedString { append(text) })
                        Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show()
                    },
                    onCloseClick = {
                        onEvent(TranslateEvent.CloseTranslation)
                    },
                    onSpeakerClick = {
                        textToSpeech.language = state.toLanguage.toLocale()
                        textToSpeech.speak(state.toText, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    onEditClick = {
                        onEvent(TranslateEvent.EditTranslation)
                    },
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.Translate)
                    }
                )
            }
            // endregion Translation Field

            // region History
            if (state.history.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.history),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                items(state.history) { historyItem ->
                    TranslateHistoryItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = historyItem,
                        onClick = {
                            onEvent(TranslateEvent.SelectHistoryItem(historyItem))
                        }
                    )
                }
            }
            // endregion History
        }
    }
}