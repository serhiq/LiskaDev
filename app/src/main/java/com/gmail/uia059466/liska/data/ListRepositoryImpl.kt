package com.gmail.uia059466.liska.data

import com.gmail.uia059466.liska.data.database.*
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfigRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListRepositoryImpl(
    private val dao: DatabaseListingDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val configRepository:WidgetConfigRepository
) : ListRepository {

    override suspend fun insert(list: ListEdit): Long {
        return withContext(ioDispatcher){
           dao.insert(ListDatabase(
               id = 0,
               title = list.title,
               order = 0,
               list = list.data,
               isCatalog = false,
               lastModificationDate = System.currentTimeMillis(),
               creationDate = System.currentTimeMillis()
           ))
        }
    }

    override suspend fun getAll(): HolderResult<List<ListDisplay>> = withContext(ioDispatcher)  {
        return@withContext try {
            HolderResult.Success(dao.getAllList().toDisplayList())
        } catch (e: Exception) {
            HolderResult.Error(e)
        }
    }

    override suspend fun updateOrder(id: Long, order: Int) {
        dao.updateOrder(id=id,order=order)
    }

    override suspend fun updateItems(id: Long, items: List<ListItem>) {
        dao.updateItemsWithDateModifications(
            datamodification = System.currentTimeMillis(),
            id = id,
            items = items
        )
    }

    override suspend fun updateTitle(id: Long, title: String) {
        dao.updateTitleDateModifications(
            datamodification = System.currentTimeMillis(),
            id = id,
            title = title
        )
    }

    override suspend fun getById(id: Long): HolderResult<ListEdit> {
        val list=dao.getListById(id)
        return if (list!=null){
             HolderResult.Success(list.toListEdit())
        }else{
            HolderResult.Error(throw Exception("такого нет"))
        }
    }

    override suspend fun deleteById(id: Long) {
        configRepository.setupDeletedListForConfig(id)
        dao.deleteById(id)
    }

    override suspend fun deleteAllList() {
        dao.deleteAll()
    }

    override suspend fun complectedTask(listId: Long, viewIndex: Int, itemStr: String) {
        val list=dao.getListById(listId)
        val result = list?.tryComplectedTask(viewIndex,itemStr)?:false
        if (result&&list!=null){
            dao.update(list)
        }
    }
}