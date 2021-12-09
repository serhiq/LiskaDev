package com.gmail.uia059466.liska.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uia059466.liska.OpenForTesting
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.InsertList
import com.gmail.uia059466.liska.domain.usecase.GetAllListUseCase
import com.gmail.uia059466.liska.domain.usecase.MessageRepository
import com.gmail.uia059466.liska.domain.usecase.UpdateOrderInList
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch

@OpenForTesting
class ListsViewModel(
  private val getAllListUseCases: GetAllListUseCase,
  private val saveNewOrderList:UpdateOrderInList,
  private val createList: InsertList,
  private val prefs: UserRepository,
  val messageRepository: MessageRepository,
  ) : ViewModel() {
  
  private val _snackbarText = SingleLiveEvent<Int>()
  val snackbarText: LiveData<Int> = _snackbarText
 
  var _sortOrder:SortOrder=prefs.readSortOrderList()
  val navigateToManualSort = SingleLiveEvent<Boolean>()

  private val _lists = SingleLiveEvent<List<ListDisplay>>()
  val lists: LiveData<List<ListDisplay>> = _lists

  val navigateToEditList = SingleLiveEvent<Long>()

  init {
    updateList()
    checkMessages()
  }

  private fun updateList(){
    _sortOrder=prefs.readSortOrderList()
    viewModelScope.launch {
      _lists.postValue(sortedTask(_sortOrder, getAllListUseCases.invoke()))
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

  private fun checkMessages(){
    val message=messageRepository.getMessage()
    if (message!=null){
      _snackbarText.value=message
    }
  }

  private fun updateMessage(){
       val message=messageRepository.getMessage()
   if (message!=null){
     _snackbarText.value=message
   }
  }
  
  fun takeAction(action: ListsAction) {
    when (action) {
      ListsAction.UpdateMessages -> updateMessage()
      is ListsAction.SortList    -> sortList(action)
      ListsAction.NewList        -> createList()
    }
  }

  private fun sortList(action: ListsAction.SortList) {
    prefs.saveSortOrderList(action.sort)
    if (action.sort == SortOrder.MANUAL_SORT) {
      viewModelScope.launch {
        saveNewOrderList.invoke(lists.value?: emptyList())
        navigateToManualSort.postValue(true)
      }
    } else {
      updateList()
    }
  }

  fun createList(){
    viewModelScope.launch {
      val id = createList.invoke()
      navigateToEditList.postValue(id)
    }
  }

  fun refreshList() {
    updateList()
  }
}