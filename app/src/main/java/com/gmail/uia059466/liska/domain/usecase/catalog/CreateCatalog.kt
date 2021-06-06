package com.gmail.uia059466.liska.domain.usecase.catalog

import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.domain.CatalogRepository

class CreateCatalog(private val repository: CatalogRepository) {
    suspend  fun invoke(title: String):Long{
        val or=repository.lastOrder()+1

        val catalog = CatalogDatabase(
            id = 0,
            title = title,
            order = or,
            list = mutableListOf(),
            lastModificationDate = System.currentTimeMillis(),
            creationDate = System.currentTimeMillis()
        )

        return repository.insert(catalog)
    }
}