package com.gmail.uia059466.liska

import com.gmail.uia059466.liska.data.WarehouseDao
import com.gmail.uia059466.liska.data.database.*
import com.gmail.uia059466.liska.domain.CatalogRepository
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.WarehouseRepository
import com.gmail.uia059466.liska.listdetail.createCurrentDataTitle
import com.gmail.uia059466.liska.stuff.Warehouse
import com.gmail.uia059466.liska.warehouse.data.WarehouseDisplay
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WarehouseRepositoryImpl(
    private val dao: WarehouseDao
) : WarehouseRepository {

    override suspend fun getAll(): List<WarehouseDisplay> {
        return  dao.getAll().map { WarehouseDisplay(it, null) }
    }

    override suspend fun updateOrder(uuid: String, order: Int) {
        dao.updateOrder(uuid, order)
    }

    override suspend fun insert(warehouse: Warehouse){
        dao.insert(warehouse)
    }


//    override suspend fun insert(list: CatalogDatabase): Long {
//        return dao.insert(list)
//    }
//
//    override suspend fun insertAll(list: List<CatalogDatabase>) {
//        dao.insert(list)
//    }
//
//    override suspend fun getAll(): HolderResult<List<CatalogDatabase>> {
//        return try {
//            HolderResult.Success(dao.getAll())
//        } catch (e: Exception) {
//            HolderResult.Error(e)
//        }
//    }
//
//    override suspend fun updateOrder(uuid: String, order: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun insert(warehouse: Warehouse): String {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun updateOrder(id: Long, order: Int) {
//        dao.updateOrder(id,order)
//            }
//
//    override suspend fun updateItems(id: Long, items: List<CatalogItem>) {
//       dao.updateItems(id,items)
//    }
//
//    override suspend fun updateTitle(id: Long, title: String) {
//        dao.updateTitleDateModifications(
//            datamodification = System.currentTimeMillis(),
//            id = id,
//            title = title
//        )
//    }
//
//    override suspend fun getById(id: Long): HolderResult<CatalogDatabase> {
//        val list=dao.getById(id)
//        return if (list!=null){
//             HolderResult.Success(list)
//        }else{
//            HolderResult.Error(throw Exception("такого нет"))
//        }
//    }
//
//    override suspend fun deleteById(id: Long) {
//        dao.deleteById(id)
//    }
//
//    override suspend fun deleteAll() {
//        dao.deleteAll()
//    }
}