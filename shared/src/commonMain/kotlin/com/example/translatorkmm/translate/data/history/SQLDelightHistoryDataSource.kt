package com.example.translatorkmm.translate.data.history

import com.example.translatorkmm.core.domain.util.CommonFlow
import com.example.translatorkmm.core.domain.util.toCommonFlow
import com.example.translatorkmm.database.TranslateDatabase
import com.example.translatorkmm.translate.domain.history.HistoryDataSource
import com.example.translatorkmm.translate.domain.history.HistoryItem
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SQLDelightHistoryDataSource(
    private val db: TranslateDatabase
): HistoryDataSource {

    private val queries = db.translateQueries
    override fun getHistory(): CommonFlow<List<HistoryItem>> =
        queries
            .getHistory()
            .asFlow()
            .mapToList()
            .map { history ->
                history.map { it.toHistoryItem() }
            }.toCommonFlow()

    override suspend fun insertHistoryItem(item: HistoryItem) {
        queries.insertHistoryEntity(
            id = item.id,
            fromLanguageCode = item.fromLanguageCode,
            fromText = item.fromText,
            toLanguageCode = item.toLanguageCode,
            toText = item.toText,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }
}