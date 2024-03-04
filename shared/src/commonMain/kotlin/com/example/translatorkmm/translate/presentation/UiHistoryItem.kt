package com.example.translatorkmm.translate.presentation

import com.example.translatorkmm.core.presentation.UiLanguage

data class UiHistoryItem(
    val id: Long,
    val fromLanguage: UiLanguage,
    val fromText: String,
    val toLanguage: UiLanguage,
    val toText: String,
)
