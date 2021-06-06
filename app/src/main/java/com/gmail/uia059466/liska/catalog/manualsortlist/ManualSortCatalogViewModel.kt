package com.gmail.uia059466.liska.catalog.manualsortlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.catalog.GetAllCatalog
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch


class ManualSortCatalogViewModel(
  private val getAll: GetAllCatalog,
  private val saveNewOrderList: SaveNewOrderCatalog,
  val prefs: UserRepository
                    ) : ViewModel() {

  var _sortOrder:SortOrder=prefs.readSortOrderCatalog()

  private val _lists = SingleLiveEvent<List<CatalogDatabase>>()
  val lists: LiveData<List<CatalogDatabase>> = _lists

  private val _runBack =  SingleLiveEvent<Boolean>()
  val  runBack: LiveData<Boolean> = _runBack

  init {
    updateList()
  }

  private fun updateList(){
    _sortOrder=prefs.readSortOrderList()
    viewModelScope.launch{
      _lists.postValue(sortedTask(_sortOrder,getAll.invoke()))

    }
  }

  private fun sortedTask(order: SortOrder, list: List<CatalogDatabase>): List<CatalogDatabase> {
    return when (order) {
      SortOrder.NEWEST_FIRST -> list.sortedByDescending { it.creationDate }
      SortOrder.A_Z -> list.sortedBy { it.title }
      SortOrder.LAST_MODIFIED -> list.sortedByDescending { it.lastModificationDate }
      SortOrder.MANUAL_SORT -> list.sortedBy { it.order }
    }
  }

  fun runBackAndSaveSorted(sortedList: List<CatalogDatabase>) {
   viewModelScope.launch{
     saveNewOrderList.invoke(sortedList)
     _runBack.postValue(true)
   }
  }
}