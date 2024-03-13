package com.example.translatorkmm.android.di

import android.app.Application
import com.example.translatorkmm.database.TranslateDatabase
import com.example.translatorkmm.translate.data.history.SQLDelightHistoryDataSource
import com.example.translatorkmm.translate.data.local.DatabaseDriverFactory
import com.example.translatorkmm.translate.data.remote.HttpClientFactory
import com.example.translatorkmm.translate.data.translate.KtorTranslateClient
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.translate.TranslateClient
import com.example.translatorkmm.translate.domain.translate.TranslateUseCase
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClientFactory().create()
    @Provides
    @Singleton
    fun provideNetworkClient(httpClient: HttpClient): TranslateClient =
         KtorTranslateClient(httpClient)

    @Provides
    @Singleton
    fun provideDatabaseDriver(application: Application): SqlDriver =
        DatabaseDriverFactory(application).create()

    @Provides
    @Singleton
    fun provideHistoryDataSource(driver: SqlDriver): HistoryDataSource =
        SQLDelightHistoryDataSource(TranslateDatabase(driver = driver))

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: TranslateClient,
        dataSource: HistoryDataSource
    ): TranslateUseCase =
        TranslateUseCase(client = client, historyDataSource = dataSource)
}