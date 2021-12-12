package com.gmail.uia059466.liska.warehouse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.WarehouseRepository
import com.gmail.uia059466.liska.domain.usecase.MessageRepository

import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.stuff.Warehouse
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import com.gmail.uia059466.liska.warehouse.WarehouseAction.*
import com.gmail.uia059466.liska.warehouse.data.WarehouseDisplay
import kotlinx.coroutines.launch
import java.util.*

class WarehouseViewModel(
  private val warehouseRepository: WarehouseRepository,
  private val prefs: UserRepository,
  private val messageRepository: MessageRepository,
  ) : ViewModel() {
  
  private val _snackbarText = SingleLiveEvent<Int>()
  val snackbarText: LiveData<Int> = _snackbarText
 
  var _sortOrder:SortOrder=prefs.readSortOrderList()
  val navigateToManualSort = SingleLiveEvent<Boolean>()

  private val _lists = SingleLiveEvent<List<WarehouseDisplay>>()
  val lists: LiveData<List<WarehouseDisplay>> = _lists

  val navigateToEditWarehouse = SingleLiveEvent<String>()

  init {
    updateList()
    checkMessages()
  }

  private fun updateList(){
    _sortOrder = prefs.readSortOrderList()
    viewModelScope.launch {
      _lists.postValue(sortedTask(_sortOrder, warehouseRepository.getAll()))
    }
  }

  private fun sortedTask(order: SortOrder, list: List<WarehouseDisplay>): List<WarehouseDisplay> {
    return when (order) {
      SortOrder.NEWEST_FIRST -> list.sortedByDescending { it.warehouse.dataCreated }
      SortOrder.A_Z -> list.sortedBy { it.warehouse.title }
      SortOrder.LAST_MODIFIED -> list.sortedByDescending { it.warehouse.dataUpdated }
      SortOrder.MANUAL_SORT -> list.sortedBy { it.warehouse.order }
    }
  }

  private fun checkMessages(){
    val message=messageRepository.getMessage()
    if (message!=null){
      _snackbarText.value = message
    }
  }

  fun takeAction(action: WarehouseAction) {
    when (action) {
      UpdateMessages -> checkMessages()
      New -> createList()
      is Sort -> sortList(action)
    }
  }

  private fun sortList(action: Sort) {
    prefs.saveSortOrderList(action.sort)
    if (action.sort == SortOrder.MANUAL_SORT) {
      viewModelScope.launch {
        var order = 0
        lists.value?.forEach { warehouseRepository.updateOrder (it.warehouse.uuid, order); order++ }
        navigateToManualSort.postValue(true)
      }
    } else {
      updateList()
    }
  }

  fun createList(){
    viewModelScope.launch {
      val lastOrder = (lists.value?.maxOf { it.warehouse.order })
      val uuid = UUID.randomUUID().toString()

      warehouseRepository.insert(Warehouse(
        uuid = uuid,
        title = "Новый склад",
        description = null,
        dataCreated = System.currentTimeMillis(),
        dataUpdated = System.currentTimeMillis(),
        order = if (lastOrder != null) lastOrder+1 else 0
      ))
      navigateToEditWarehouse.postValue(uuid)
    }
  }

  fun refreshList() {
    updateList()
  }
}