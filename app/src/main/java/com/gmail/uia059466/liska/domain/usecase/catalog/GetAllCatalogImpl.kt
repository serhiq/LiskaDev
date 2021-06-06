package com.gmail.uia059466.liska.domain.usecase.catalog

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.domain.CatalogRepository

class GetAllCatalogImpl(private val repository: CatalogRepository):GetAllCatalog {
    override suspend fun invoke():List<CatalogDatabase>{
        val result=repository.getAll()
        return when(result){
            is HolderResult.Success -> result.data
            is HolderResult.Error ->return emptyList()
        }
    }
}

interface GetAllCatalog{
    suspend fun invoke():List<CatalogDatabase>
}