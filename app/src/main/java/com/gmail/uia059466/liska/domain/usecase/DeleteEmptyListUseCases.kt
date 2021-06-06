package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.domain.ListRepository

class DeleteEmptyListUseCases(private val repository: ListRepository, val messageRepository: MessageRepository) {
  suspend fun invoke(id:Long){
        val deleteMessage= R.string.delete_empty_list
        messageRepository.saveMessage(deleteMessage)
        repository.deleteById(id)
    }
}

