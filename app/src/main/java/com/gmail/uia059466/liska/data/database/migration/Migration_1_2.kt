package com.gmail.uia059466.liska.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration_1_2: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `Warehouses` (`uuid` TEXT NOT NULL, `title` TEXT NOT NULL , `description` TEXT, `data_created` INTEGER NOT NULL, `data_update`  INTEGER NOT NULL, `order`  INTEGER NOT NULL, PRIMARY KEY(`uuid`));")
    }
}