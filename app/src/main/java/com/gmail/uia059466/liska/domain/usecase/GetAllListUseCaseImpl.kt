package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.ListRepository

class GetAllListUseCaseImpl(private val repository: ListRepository):GetAllListUseCase {
    override suspend fun invoke():List<ListDisplay>{
        val result=repository.getAll()
        return when(result){
            is HolderResult.Success -> result.data
            is HolderResult.Error ->return emptyList()
        }
    }
}

interface GetAllListUseCase{
    suspend fun invoke():List<ListDisplay>
}