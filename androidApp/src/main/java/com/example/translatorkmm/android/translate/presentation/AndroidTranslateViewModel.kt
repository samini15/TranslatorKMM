package com.example.translatorkmm.android.translate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.translate.TranslateUseCase
import com.example.translatorkmm.translate.presentation.TranslateEvent
import com.example.translatorkmm.translate.presentation.TranslateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val historyDataSource: HistoryDataSource
) : ViewModel() {
    private val viewModel by lazy {
        TranslateViewModel(translateUseCase, historyDataSource, viewModelScope)
    }

    val state = viewModel.state

    fun onEvent(event: TranslateEvent) {
        viewModel.onEvent(event)
    }
}