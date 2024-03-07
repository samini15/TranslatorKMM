package com.example.translatorkmm.translate.presentation

import com.example.translatorkmm.core.domain.util.Resource
import com.example.translatorkmm.core.domain.util.toCommonStateFlow
import com.example.translatorkmm.core.presentation.UiLanguage
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.translate.TranslateException
import com.example.translatorkmm.translate.domain.translate.TranslateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translateUseCase: TranslateUseCase,
    private val historyDataSource: HistoryDataSource,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(TranslateState())
    val state = combine(
        _state,
        historyDataSource.getHistory()
    ) { state, history ->
        if (state.history != history) {
            state.copy(
                history = history.mapNotNull { item ->
                    UiHistoryItem(
                        id = item.id ?: return@mapNotNull null,
                        fromText = item.fromText,
                        toText = item.toText,
                        fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                        toLanguage = UiLanguage.byCode(item.toLanguageCode)
                    )
                }
            )
        } else state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TranslateState()).toCommonStateFlow()

    private var translateJob: Job? = null

    fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.ChangeTranslationText -> {
                _state.update {
                    it.copy(
                        fromText = event.text
                    )
                }
            }
            is TranslateEvent.ChooseFromLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = false,
                        fromLanguage = event.language
                    )
                }
            }
            is TranslateEvent.ChooseToLanguage -> {
                val newState = _state.updateAndGet {
                    it.copy(
                        isChoosingToLanguage = false,
                        toLanguage = event.language
                    )
                }
                translate(newState)
            }
            TranslateEvent.CloseTranslation -> {
                _state.update {
                    it.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null
                    )
                }
            }
            TranslateEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            toText = null
                        )
                    }
                }
            }
            TranslateEvent.OnErrorSeen -> {
                _state.update {
                    it.copy(error = null)
                }
            }
            TranslateEvent.OpenFromLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = true
                    )
                }
            }
            TranslateEvent.OpenToLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = true
                    )
                }
            }
            TranslateEvent.RecordAudio -> TODO()
            is TranslateEvent.SelectHistoryItem -> {
                translateJob?.cancel()
                _state.update {
                    it.copy(
                        fromLanguage = event.historyItem.fromLanguage,
                        fromText = event.historyItem.fromText,
                        toLanguage = event.historyItem.toLanguage,
                        toText = event.historyItem.toText,
                        isTranslating = false
                    )
                }
            }
            TranslateEvent.StopChoosingLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = false,
                        isChoosingToLanguage = false
                    )
                }
            }
            is TranslateEvent.SubmitVoiceResult -> {
                _state.update {
                    it.copy(
                        fromText = event.transcribedAudio ?: it.fromText,
                        isTranslating = if (event.transcribedAudio != null) false else it.isTranslating,
                        toText = if (event.transcribedAudio != null) null else it.toText
                    )
                }
            }
            TranslateEvent.SwapLanguages -> {
                _state.update {
                    it.copy(
                        fromLanguage = it.toLanguage,
                        toLanguage = it.fromLanguage,
                        fromText = it.toText.orEmpty(),
                        toText = if (it.toText != null) it.fromText else null
                    )
                }
            }
            TranslateEvent.Translate -> translate(state.value)
        }
    }
    private fun translate(state: TranslateState) {
        if (state.isTranslating || state.fromText.isBlank()) {
            return
        }

        translateJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isTranslating = true
                )
            }
            state.apply {
                val result = translateUseCase(
                    fromLanguage = state.fromLanguage.language,
                    fromText = state.fromText,
                    toLanguage = state.toLanguage.language
                )

                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isTranslating = false,
                                toText = result.data
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isTranslating = false,
                                error = (result.throwable as? TranslateException)?.error
                            )
                        }
                    }
                }
            }


        }
    }
}