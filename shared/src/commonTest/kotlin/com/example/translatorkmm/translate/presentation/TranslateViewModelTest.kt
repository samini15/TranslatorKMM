package com.example.translatorkmm.translate.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.example.translatorkmm.core.presentation.UiLanguage
import com.example.translatorkmm.translate.data.local.FakeHistoryDataSource
import com.example.translatorkmm.translate.data.remote.FakeTranslateClient
import com.example.translatorkmm.translate.domain.history.HistoryItem
import com.example.translatorkmm.translate.domain.translate.TranslateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class TranslateViewModelTest {

    private lateinit var viewModel: TranslateViewModel

    private lateinit var dataSource: FakeHistoryDataSource
    private lateinit var client: FakeTranslateClient
    private lateinit var useCase: TranslateUseCase

    @BeforeTest
    fun setUp() {
        dataSource = FakeHistoryDataSource()
        client = FakeTranslateClient()
        useCase = TranslateUseCase(client, dataSource)
        viewModel = TranslateViewModel(
            translateUseCase = useCase,
            historyDataSource = dataSource,
            coroutineScope = CoroutineScope(Dispatchers.Default)
        )
    }

    @Test
    fun `State and history items properly combined`() = runBlocking {
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(TranslateState())

            val item = HistoryItem(
                id = 0,
                fromLanguageCode = "fr",
                fromText = "Bonjour",
                toLanguageCode = "en",
                toText = "Hello"
            )
            dataSource.insertHistoryItem(item)

            val state = awaitItem()

            val expected = UiHistoryItem(
                id = item.id!!,
                fromText = item.fromText,
                toText = item.toText,
                fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                toLanguage = UiLanguage.byCode(item.toLanguageCode)
            )
            assertThat(state.history.first()).isEqualTo(expected)
        }
    }

    @Test
    fun `Translate success - state properly updated`() = runBlocking {
        viewModel.state.test {
            awaitItem()

            viewModel.onEvent(TranslateEvent.ChangeTranslationText("test"))
            awaitItem()

            viewModel.onEvent(TranslateEvent.Translate)
            val loadingState = awaitItem()

            assertThat(loadingState.isTranslating).isTrue()

            val resultState = awaitItem()
            assertThat(resultState.isTranslating).isFalse()
            assertThat(resultState.toText).isEqualTo(client.translatedText)
        }
    }
}