package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.domain.ListRepository

class UpdateListTitleImpl(private val repository: ListRepository):UpdateListTitle {
    override suspend fun invoke(id:Long,title:String){
        repository.updateTitle(id,title)
    }
}

interface UpdateListTitle{
    suspend fun invoke(id:Long,title:String)
}