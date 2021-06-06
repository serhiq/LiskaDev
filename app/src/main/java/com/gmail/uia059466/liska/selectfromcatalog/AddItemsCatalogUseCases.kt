package com.gmail.uia059466.liska.selectfromcatalog

import com.gmail.uia059466.liska.catalog.AddItemsInCatalog
import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.domain.CatalogRepository

class AddItemsCatalogUseCase(private val repository: CatalogRepository):AddItemsCatalogUkase {
    override suspend fun invoke(commands: List<AddItemsInCatalog>){
        val ids= mutableListOf<Long>()
        for (c in commands){
            if (!ids.contains(c.id)) ids.add(c.id)
        }

        for (id in ids){
            val newItems=commands.filter { it.id==id }.map { CatalogItem(
                title = it.title,
                quantity = 0,
                unit = "",
                isSelected = false
            ) }
            val old=repository.getById(id)
            if (old is HolderResult.Success){
                val newData=old.data.list+newItems
                repository.updateItems(id,newData)
            }
        }
    }
}

interface AddItemsCatalogUkase{
    suspend fun invoke(commands: List<AddItemsInCatalog>)
}