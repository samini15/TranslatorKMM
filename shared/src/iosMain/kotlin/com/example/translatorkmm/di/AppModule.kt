package com.example.translatorkmm.di

import com.example.translatorkmm.database.TranslateDatabase
import com.example.translatorkmm.translate.data.history.SQLDelightHistoryDataSource
import com.example.translatorkmm.translate.data.local.DatabaseDriverFactory
import com.example.translatorkmm.translate.data.remote.HttpClientFactory
import com.example.translatorkmm.translate.data.translate.KtorTranslateClient
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.translate.TranslateClient
import com.example.translatorkmm.translate.domain.translate.TranslateUseCase
import com.example.translatorkmm.voice_to_text.domain.VoiceToTextParser

interface AppModule {
    val historyDataSource: HistoryDataSource
    val translateClient: TranslateClient
    val translateUseCase: TranslateUseCase
    val voiceToTextParser: VoiceToTextParser
}
class AppModuleImpl(
    parser: VoiceToTextParser
): AppModule {

    override val historyDataSource: HistoryDataSource by lazy {
        SQLDelightHistoryDataSource(TranslateDatabase(driver = DatabaseDriverFactory().create()))
    }

    override val translateClient: TranslateClient by lazy {
        KtorTranslateClient(httpClient = HttpClientFactory().create())
    }

    override val translateUseCase: TranslateUseCase by lazy {
        TranslateUseCase(client = translateClient, historyDataSource = historyDataSource)
    }

    override val voiceToTextParser = parser
}