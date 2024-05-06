package com.example.translatorkmm.di

import com.example.translatorkmm.testing.FakeHistoryDataSource
import com.example.translatorkmm.testing.FakeTranslateClient
import com.example.translatorkmm.testing.FakeVoiceToTextParser
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.translate.TranslateClient
import com.example.translatorkmm.translate.domain.translate.TranslateUseCase
import com.example.translatorkmm.voice_to_text.domain.VoiceToTextParser

class TestAppModule: AppModule {
    override val historyDataSource: HistoryDataSource
        = FakeHistoryDataSource()
    override val translateClient: TranslateClient
        = FakeTranslateClient()
    override val translateUseCase: TranslateUseCase
        = TranslateUseCase(translateClient, historyDataSource)
    override val voiceToTextParser: VoiceToTextParser
        = FakeVoiceToTextParser()
}