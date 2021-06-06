package com.gmail.uia059466.liska.domain.usecase.catalog

import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.domain.CatalogRepository

class UpdateCatalogItemsImpl(private val repository: CatalogRepository):UpdateCatalogItems {
    override suspend fun invoke(id:Long,items:List<CatalogItem>){
        repository.updateItems(id,items)
    }
}

interface UpdateCatalogItems{
    suspend fun invoke(id:Long,items:List<CatalogItem>)
}