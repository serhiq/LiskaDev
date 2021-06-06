package com.gmail.uia059466.liska.domain.usecase.catalog

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.domain.CatalogRepository

class GetCatalogImpl(private val repository: CatalogRepository):GetCatalog {
    override suspend fun invoke(id:Long):CatalogDatabase?{
        val result=repository.getById(id)
        return when(result){
            is HolderResult.Success -> result.data
            is HolderResult.Error -> null
        }
    }
}

interface GetCatalog{
    suspend fun invoke(id:Long):CatalogDatabase?
}