package com.example.translatorkmm.di

import com.example.translatorkmm.translate.data.local.FakeHistoryDataSource
import com.example.translatorkmm.translate.data.remote.FakeTranslateClient
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.translate.TranslateClient
import com.example.translatorkmm.translate.domain.translate.TranslateUseCase
import com.example.translatorkmm.voiceToText.data.FakeVoiceToTextParser
import com.example.translatorkmm.voice_to_text.domain.VoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideFakeTranslateClient(): TranslateClient =
        FakeTranslateClient()

    @Provides
    @Singleton
    fun provideFakeHistoryDataSource(): HistoryDataSource =
        FakeHistoryDataSource()

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: TranslateClient,
        dataSource: HistoryDataSource
    ): TranslateUseCase =
        TranslateUseCase(client, dataSource)

    @Provides
    @Singleton
    fun provideFakeVoiceToTextParser(): VoiceToTextParser =
        FakeVoiceToTextParser()
}