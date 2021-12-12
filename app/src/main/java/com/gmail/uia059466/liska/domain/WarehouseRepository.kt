package com.gmail.uia059466.liska.domain

import com.gmail.uia059466.liska.stuff.Warehouse
import com.gmail.uia059466.liska.warehouse.data.WarehouseDisplay

interface WarehouseRepository {
    suspend fun getAll():List<WarehouseDisplay>
    suspend fun updateOrder(uuid: String, order: Int)
    suspend fun insert(warehouse: Warehouse)
//    suspend fun insert(list:ListEdit):Long
//    suspend fun getAll(): HolderResult<List<ListDisplay>>
//    suspend fun getById(id:Long): HolderResult<ListEdit>
//    suspend fun updateOrder(id: Long, order: Int)
//    suspend fun deleteById(id: Long)
//    suspend fun updateItems(id:Long,items:List<ListItem>)
//    suspend fun updateTitle(id:Long,title:String)
//    suspend fun deleteAllList()
//    suspend fun complectedTask(listId: Long, viewIndex: Int, itemStr: String)
}
