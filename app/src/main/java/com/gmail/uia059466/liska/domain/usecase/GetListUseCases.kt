package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.domain.ListRepository

class GetListUseCasesImpl(private val repository: ListRepository):GetListUseCases {
    override suspend fun invoke(id:Long):HolderResult<ListEdit>{
        return repository.getById(id)

    }
}

interface GetListUseCases{
    suspend fun invoke(id:Long):HolderResult<ListEdit>
}