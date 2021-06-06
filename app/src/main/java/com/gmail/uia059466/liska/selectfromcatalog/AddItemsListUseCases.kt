package com.gmail.uia059466.liska.selectfromcatalog

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.ListItem
import com.gmail.uia059466.liska.domain.ListRepository

class AddItemsUneaseImpl(private val repository: ListRepository):AddItemsListUsecase {
    override suspend fun invoke(id:Long,items:List<String>){

        val newItems=items.map { ListItem(title = it,isChecked = false, isSelected = false) }

        val list=repository.getById(id)
        if (list  is HolderResult.Success){
            val newData= list.data.data+newItems
            repository.updateItems(id,newData)
        }
    }
}

interface AddItemsListUsecase{
    suspend fun invoke(id:Long,items:List<String>)
}