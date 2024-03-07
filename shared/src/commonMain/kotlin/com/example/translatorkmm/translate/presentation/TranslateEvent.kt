package com.example.translatorkmm.translate.presentation

import com.example.translatorkmm.core.presentation.UiLanguage

sealed class TranslateEvent {
    data class ChooseFromLanguage(val language: UiLanguage): TranslateEvent()

    data class ChooseToLanguage(val language: UiLanguage): TranslateEvent()

    data object StopChoosingLanguage: TranslateEvent()

    data object SwapLanguages: TranslateEvent()

    data class ChangeTranslationText(val text: String): TranslateEvent()

    data object Translate: TranslateEvent()

    data object OpenFromLanguageDropDown: TranslateEvent()

    data object OpenToLanguageDropDown: TranslateEvent()

    data object CloseTranslation: TranslateEvent()

    data class SelectHistoryItem(val historyItem: UiHistoryItem): TranslateEvent()

    data object EditTranslation: TranslateEvent()

    data object RecordAudio: TranslateEvent()

    data class SubmitVoiceResult(val transcribedAudio: String?): TranslateEvent()

    data object OnErrorSeen: TranslateEvent()
}