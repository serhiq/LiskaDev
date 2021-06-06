package com.gmail.uia059466.liska.domain

import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.data.database.ListItem

interface ListRepository {
    suspend fun insert(list:ListEdit):Long
    suspend fun getAll(): HolderResult<List<ListDisplay>>
    suspend fun getById(id:Long): HolderResult<ListEdit>
    suspend fun updateOrder(id: Long, order: Int)
    suspend fun deleteById(id: Long)
    suspend fun updateItems(id:Long,items:List<ListItem>)
    suspend fun updateTitle(id:Long,title:String)
    suspend fun deleteAllList()
    suspend fun complectedTask(listId: Long, viewIndex: Int, itemStr: String)
}
