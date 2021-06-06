package com.gmail.uia059466.liska.manualsortlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.GetAllListUseCase
import com.gmail.uia059466.liska.domain.usecase.UpdateOrderInList
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch


class ManualSortViewModel(
    private val getAllListUseCases: GetAllListUseCase,
    private val saveNewOrderList: UpdateOrderInList,
    val prefs: UserRepository
                    ) : ViewModel() {

  var _sortOrder:SortOrder=prefs.readSortOrderList()

  private val _lists = SingleLiveEvent<List<ListDisplay>>()
  val lists: LiveData<List<ListDisplay>> = _lists

  private val _runBack =  SingleLiveEvent<Boolean>()
  val  runBack: LiveData<Boolean> = _runBack

  init {
    updateList()
  }

  private fun updateList(){
    _sortOrder=prefs.readSortOrderList()
    viewModelScope.launch{
      _lists.postValue(sortedTask(_sortOrder,getAllListUseCases.invoke()))

    }
  }

  private fun sortedTask(order: SortOrder, list: List<ListDisplay>): List<ListDisplay> {
    return when (order) {
      SortOrder.NEWEST_FIRST -> list.sortedByDescending { it.dataCreate }
      SortOrder.A_Z -> list.sortedBy { it.title }
      SortOrder.LAST_MODIFIED -> list.sortedByDescending { it.dataModification }
      SortOrder.MANUAL_SORT -> list.sortedBy { it.order }
    }
  }

  fun runBackAndSaveSorted(sortedList: List<ListDisplay>) {
   viewModelScope.launch{
     saveNewOrderList.invoke(sortedList)
     _runBack.postValue(true)
   }
  }
}