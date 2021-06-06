package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.domain.ListRepository

class UpdateListImpl(private val repository: ListRepository, val messageRepository: MessageRepository):UpdateList {
    override suspend fun invoke(listEdit: ListEdit){
        repository.updateItems(listEdit.id,listEdit.data)
        repository.updateTitle(listEdit.id,listEdit.title)

        messageRepository.saveMessage(R.string.saved)
    }
}

interface UpdateList{
    suspend fun invoke(listEdit: ListEdit)
}