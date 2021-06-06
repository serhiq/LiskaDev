package com.gmail.uia059466.liska.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gmail.uia059466.liska.data.CatalogItemTypeConverter
import com.gmail.uia059466.liska.data.ListDisplay

data class CatalogDatabaseEdit(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val title: String,
        val order: Int,

        val list: MutableList<CatalogItem>,
        val lastModificationDate:Long,
        val creationDate:Long)


 fun List<CatalogDatabase>.toCatalogDatabaseEdit():MutableList<CatalogDatabaseEdit>{
         return this.map { CatalogDatabaseEdit(
                 id = it.id,
                 title = it.title,
                 order = it.order,
                 list =it.list.toMutableList(),
                 lastModificationDate = it.lastModificationDate,
                 creationDate = it.creationDate
         ) }.toMutableList()

}


fun List<CatalogDatabaseEdit>.asCatalogDatabaseEdit():List<CatalogDatabase>{
        return this.map { CatalogDatabase(
                id = it.id,
                title = it.title,
                order = it.order,
                list =it.list.toMutableList(),
                lastModificationDate = it.lastModificationDate,
                creationDate = it.creationDate
        ) }.toMutableList()

}