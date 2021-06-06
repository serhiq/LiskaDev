package com.gmail.uia059466.liska.data

import com.gmail.uia059466.liska.data.database.*
import com.gmail.uia059466.liska.domain.CatalogRepository
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.listdetail.createCurrentDataTitle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatalogRepositoryImpl(
    private val dao: CatalogDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CatalogRepository {


    override suspend fun lastOrder(): Int {
        return dao.lastOrder()?:0
    }

    override suspend fun insert(list: CatalogDatabase): Long {
        return dao.insert(list)
    }

    override suspend fun insertAll(list: List<CatalogDatabase>) {
        dao.insert(list)
    }

    override suspend fun getAll(): HolderResult<List<CatalogDatabase>> {
        return try {
            HolderResult.Success(dao.getAll())
        } catch (e: Exception) {
            HolderResult.Error(e)
        }
    }

    override suspend fun updateOrder(id: Long, order: Int) {
        dao.updateOrder(id,order)
            }

    override suspend fun updateItems(id: Long, items: List<CatalogItem>) {
       dao.updateItems(id,items)
    }

    override suspend fun updateTitle(id: Long, title: String) {
        dao.updateTitleDateModifications(
            datamodification = System.currentTimeMillis(),
            id = id,
            title = title
        )
    }

    override suspend fun getById(id: Long): HolderResult<CatalogDatabase> {
        val list=dao.getById(id)
        return if (list!=null){
             HolderResult.Success(list)
        }else{
            HolderResult.Error(throw Exception("такого нет"))
        }
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}