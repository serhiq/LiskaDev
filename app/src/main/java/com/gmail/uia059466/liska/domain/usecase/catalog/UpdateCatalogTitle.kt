package com.gmail.uia059466.liska.domain.usecase.catalog

import com.gmail.uia059466.liska.domain.CatalogRepository

class UpdateCatalogTitle(private val repository: CatalogRepository) {
    suspend fun invoke(id:Long,title:String){
        repository.updateTitle(id,title)
    }
}
