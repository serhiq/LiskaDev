package com.gmail.uia059466.liska.catalog.manualsortlist

import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.domain.CatalogRepository
import com.gmail.uia059466.liska.domain.ListRepository

class SaveNewOrderCatalogImpl(private val repository: CatalogRepository):SaveNewOrderCatalog {
    override suspend fun invoke( newList: List<CatalogDatabase>) {
        var order=0
        for (m in newList){
                repository.updateOrder(m.id,order)
            order+=100
        }
    }
}

interface SaveNewOrderCatalog{
    suspend fun invoke(newList:List<CatalogDatabase>)
}