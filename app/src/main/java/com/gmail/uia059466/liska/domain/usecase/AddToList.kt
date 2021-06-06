package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.ListItem
import com.gmail.uia059466.liska.domain.ListRepository

class AddToListImpl(private val repository: ListRepository, val messageRepository: MessageRepository):AddToList {
    override suspend fun invoke(id:Long,selected:List<ListItem>){
        val oldList=repository.getById(id)
        if (oldList is HolderResult.Success){
            val newData=oldList.data.data+selected
            repository.updateItems(id,newData)
        }
    }
}

interface AddToList{
    suspend fun invoke(id:Long,selected:List<ListItem>)
}