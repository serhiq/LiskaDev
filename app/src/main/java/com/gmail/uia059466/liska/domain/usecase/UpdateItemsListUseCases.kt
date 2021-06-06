package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.data.database.ListItem
import com.gmail.uia059466.liska.domain.ListRepository

class UpdateItemsInList(private val repository: ListRepository):UpdateItemsListUsecase {
    override suspend fun invoke(id:Long,items:List<ListItem>){
        repository.updateItems(id,items)
    }
}

interface UpdateItemsListUsecase{
    suspend fun invoke(id:Long,items:List<ListItem>)
}