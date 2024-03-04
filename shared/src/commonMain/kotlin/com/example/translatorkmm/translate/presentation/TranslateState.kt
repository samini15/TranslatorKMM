package com.example.translatorkmm.translate.presentation

import com.example.translatorkmm.core.presentation.UiLanguage
import com.example.translatorkmm.translate.domain.translate.TranslateError

data class TranslateState(
    val fromLanguage: UiLanguage = UiLanguage.byCode("fr"),
    val fromText: String = "",
    val isChoosingFromLanguage: Boolean = false,
    val toLanguage: UiLanguage = UiLanguage.byCode("nl"),
    val toText: String? = null,
    val isChoosingToLanguage: Boolean = false,
    val isTranslating: Boolean = false,
    val error: TranslateError? = null,
    val history: List<UiHistoryItem> = emptyList()
)
