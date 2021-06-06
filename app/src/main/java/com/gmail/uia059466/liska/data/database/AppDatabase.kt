package com.gmail.uia059466.liska.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gmail.uia059466.liska.data.CatalogItemTypeConverter

@Database(
    entities = [
        ListDatabase::class,
        CatalogDatabase::class
    ],
    version = 1,
    exportSchema = false
         )

@TypeConverters(
    DisplayItemTypeConverter::class,
    CatalogItemTypeConverter::class
               )
abstract class AppDatabase : RoomDatabase() {
    abstract fun listingDao(): DatabaseListingDao
    abstract fun catalogDao(): CatalogDao
}