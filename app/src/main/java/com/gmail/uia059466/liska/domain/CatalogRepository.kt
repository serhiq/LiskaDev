package com.gmail.uia059466.liska.domain

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.data.database.CatalogItem

interface CatalogRepository {
    suspend fun insert(list: CatalogDatabase): Long
    suspend fun insertAll(list: List<CatalogDatabase>)
    suspend fun lastOrder(): Int
    suspend fun getAll(): HolderResult<List<CatalogDatabase>>
    suspend fun getById(id: Long): HolderResult<CatalogDatabase>
    suspend fun updateOrder(id: Long, order: Int)
    suspend fun deleteById(id: Long)
    suspend fun updateTitle(id: Long, title: String)
    suspend fun updateItems(id: Long, items: List<CatalogItem>)
    suspend fun deleteAll()
}
