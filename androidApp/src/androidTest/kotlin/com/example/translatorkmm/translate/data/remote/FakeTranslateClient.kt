package com.example.translatorkmm.translate.data.remote

import com.example.translatorkmm.core.domain.language.Language
import com.example.translatorkmm.translate.domain.translate.TranslateClient

class FakeTranslateClient: TranslateClient {

    var translatedText = "test translation"

    override suspend fun translate(
        fromLanguage: Language,
        text: String,
        toLanguage: Language
    ): String = translatedText
}