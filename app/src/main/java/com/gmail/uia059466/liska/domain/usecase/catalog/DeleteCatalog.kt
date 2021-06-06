package com.gmail.uia059466.liska.domain.usecase.catalog

import com.gmail.uia059466.liska.domain.CatalogRepository

class DeleteCatalogImpl(private val repository: CatalogRepository):DeleteCatalog {
    override suspend fun invoke(id:Long){
        repository.deleteById(id)
    }
}

interface DeleteCatalog{
    suspend fun invoke(id:Long)
}