package com.example.translatorkmm.translate.data.translate

import Constants
import com.example.translatorkmm.core.domain.language.Language
import com.example.translatorkmm.translate.domain.translate.TranslateClient
import com.example.translatorkmm.translate.domain.translate.TranslateError
import com.example.translatorkmm.translate.domain.translate.TranslateException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException

class KtorTranslateClient(
    private val httpClient: HttpClient
): TranslateClient {
    override suspend fun translate(
        fromLanguage: Language,
        text: String,
        toLanguage: Language
    ): String {
        val result = try {
            httpClient.post {
                url(Constants.BASE_URL + "/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateDto(
                        textToTranslate = text,
                        sourceLanguageCode = fromLanguage.langCode,
                        targetLanguageCode = toLanguage.langCode
                    )
                )
            }
        } catch (e: IOException) {
            throw TranslateException(error = TranslateError.SERVICE_UNAVAILABLE)
        }

        when (result.status.value) {
            in 200..299 -> Unit
            in 400..499 -> throw TranslateException(TranslateError.CLIENT_ERROR)
            500 -> throw TranslateException(TranslateError.SERVER_ERROR)
            else -> throw TranslateException(TranslateError.UNKNOWN_ERROR)

        }

        return try {
            result.body<TranslatedDto>().translatedText
        } catch (e: Exception) {
            throw TranslateException(TranslateError.SERVER_ERROR)
        }
    }
}