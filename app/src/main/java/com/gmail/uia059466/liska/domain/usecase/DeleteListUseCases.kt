package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.domain.ListRepository

class DeleteListUseCasesImpl(private val repository: ListRepository, val messageRepository: MessageRepository):DeleteListUseCases {
    override suspend fun invoke(id:Long){
        val deleteMessage= R.string.delete_list_message
        messageRepository.saveMessage(deleteMessage)
        repository.deleteById(id)

    }
}

interface DeleteListUseCases{
    suspend fun invoke(id:Long)
}