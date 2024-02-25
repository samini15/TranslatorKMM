package com.example.translatorkmm.translate.data.local

import com.example.translatorkmm.database.TranslateDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            schema = TranslateDatabase.Schema,
            name = "translate.db"
        )
    }
}