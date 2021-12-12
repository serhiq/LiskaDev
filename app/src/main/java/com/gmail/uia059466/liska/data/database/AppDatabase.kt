package com.gmail.uia059466.liska.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gmail.uia059466.liska.data.CatalogItemTypeConverter
import com.gmail.uia059466.liska.data.WarehouseDao
import com.gmail.uia059466.liska.stuff.Warehouse

@Database(
    entities = [
        ListDatabase::class,
        CatalogDatabase::class,
        Warehouse::class
    ],
    version = 2,
    exportSchema = false
         )

@TypeConverters(
    DisplayItemTypeConverter::class,
    CatalogItemTypeConverter::class
               )
abstract class AppDatabase : RoomDatabase() {
    abstract fun listingDao(): DatabaseListingDao
    abstract fun catalogDao(): CatalogDao
    abstract fun warehouseDao(): WarehouseDao
}