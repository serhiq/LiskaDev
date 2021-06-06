package com.gmail.uia059466.liska.domain.usecase

import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.domain.ListRepository
import java.text.SimpleDateFormat
import java.util.*

class InsertList(private val repository: ListRepository) {
    suspend  fun invoke():Long{
        val list =ListEdit(
            id = 0,
            title = fillingTempTitle(), data = mutableListOf()
        )
        return repository.insert(list)
    }

    private fun fillingTempTitle(): String {
        val currentDate: String =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        return "($currentDate)"
    }
}