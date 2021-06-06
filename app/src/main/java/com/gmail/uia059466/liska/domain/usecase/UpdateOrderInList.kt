package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.ListRepository

class UpdateOrderInListImpl(private val repository: ListRepository):UpdateOrderInList {
    override suspend fun invoke( newList: List<ListDisplay>) {
        var order=0
        for (m in newList){
                repository.updateOrder(m.id,order)
            order+=100
        }
    }
}

interface UpdateOrderInList{
    suspend fun invoke(newList:List<ListDisplay>)
}