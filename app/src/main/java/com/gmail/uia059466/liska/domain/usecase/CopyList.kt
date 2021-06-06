package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.usecase.CreateTitleWithCounter.Companion.createNextTitle

class CopyList(private val repository: ListRepository, val messageRepository: MessageRepository):CopyListUseCase {
    override suspend fun invoke(idList: Long){
        val old=repository.getById(idList)

        if (old is HolderResult.Success){
            repository.insert(old.data.copy(title = createCountedTitle(old.data.title)))
        }
        messageRepository.saveMessage(R.string.copy_list_message)
    }

    private fun createCountedTitle(title: String): String {
        return createNextTitle(title)
    }
}

interface CopyListUseCase{
    suspend fun invoke(idList:Long)
}

class CreateTitleWithCounter(){

    companion object{
        val pattern = "\\(\\d\\)"

        fun createNextTitle(title:String):String{
            val number=findNumberWithSq(title)
            if (number!=null){
                return  Regex(pattern).replace(title, "(${number+1})")
            } else{
                return "$title (2)"
            }
        }


        fun findNumberWithSq(title: String): Int? {
            val matchedResults = Regex(pattern = pattern).findAll(input = title)
            val result = StringBuilder()
            for (matchedText in matchedResults) {
                result.append(matchedText.value)
            }
            return if (result.length==3){
                val first=result[1]
                return first.toString().toIntOrNull()
            }else{
                null
            }
        }
    }
}