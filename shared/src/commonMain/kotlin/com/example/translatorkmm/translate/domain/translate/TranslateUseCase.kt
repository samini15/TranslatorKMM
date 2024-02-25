package com.example.translatorkmm.translate.domain.translate

import com.example.translatorkmm.core.domain.language.Language
import com.example.translatorkmm.core.domain.util.Resource
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.history.HistoryItem

class TranslateUseCase(
    private val client: TranslateClient,
    private val historyDataSource: HistoryDataSource
) {
    suspend operator fun invoke(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> {
        return try {
            val translatedText = client.translate(fromLanguage, fromText, toLanguage)

            // Save to db
            historyDataSource.insertHistoryItem(
                HistoryItem(
                    id = null,
                    fromLanguageCode = fromLanguage.langCode,
                    fromText = fromText,
                    toLanguageCode = toLanguage.langCode,
                    toText = translatedText
                )
            )

            Resource.Success(translatedText)
        } catch (ex: TranslateException) {
            ex.printStackTrace()
            Resource.Error(ex)
        }
    }
}