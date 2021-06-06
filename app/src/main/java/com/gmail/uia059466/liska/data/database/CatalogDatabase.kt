package com.gmail.uia059466.liska.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gmail.uia059466.liska.data.CatalogItemTypeConverter
import com.gmail.uia059466.liska.data.ListDisplay

@Entity(tableName = "catalog")
data class CatalogDatabase(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val title: String,
        val order: Int,

        @TypeConverters(CatalogItemTypeConverter::class)
        val list: List<CatalogItem>,
        val lastModificationDate:Long,
        val creationDate:Long)

