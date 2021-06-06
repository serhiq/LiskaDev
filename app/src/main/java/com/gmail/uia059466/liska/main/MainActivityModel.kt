package com.gmail.uia059466.liska.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uia059466.liska.OpenForTesting
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.InsertList
import com.gmail.uia059466.liska.domain.usecase.GetAllListUseCase
import com.gmail.uia059466.liska.domain.usecase.MessageRepository
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.main.navigationdrawer.NavigationItemModel
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch

@OpenForTesting
class MainActivityModel(
  private val getAllListUseCases: GetAllListUseCase,
  private val createList: InsertList,
  private val prefs: UserRepository
                    ) : ViewModel() {
  

  var _sortOrder:SortOrder=prefs.readSortOrderList()
  val navigateToManualSort = SingleLiveEvent<Boolean>()

  private val _lists = SingleLiveEvent<List<NavigationItemModel>>()
  val lists: LiveData<List<NavigationItemModel>> = _lists


  val toDisplayList = SingleLiveEvent<Long>()
  val toNewList = SingleLiveEvent<Long>()
  val toEditList = SingleLiveEvent<Long>()

  val currentRvPosition = SingleLiveEvent<Long>()
  val currentDrawerPosition = SingleLiveEvent<DrawerList>()

  init {
    updateList()
  }

  private fun updateList(){
    _sortOrder=prefs.readSortOrderList()
    viewModelScope.launch {
      _lists.postValue(displayedList())
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

  suspend fun displayedList():List<NavigationItemModel>
  {
   val raw= sortedTask(_sortOrder,getAllListUseCases.invoke())
    return raw.map { NavigationItemModel(title = it.title,idList = it.id) }
  }

  fun takeAction(action: MainAction) {
    when(action){
      MainAction.NewList ->createList()
       is MainAction.ToList -> {
        toDisplayList.postValue(action.id)
        currentRvPosition.postValue(action.id)
        currentDrawerPosition.postValue(DrawerList.Empty)
      }

      is MainAction.hightLightList -> {
        currentRvPosition.postValue(-1)
        currentDrawerPosition.postValue(DrawerList.LISTS)
      }
      MainAction.hightLightCatalog -> {
        currentRvPosition.postValue(-1)
        currentDrawerPosition.postValue(DrawerList.Catalog)
      }

      is MainAction.SelectedList -> {
        currentRvPosition.postValue(action.id)
        currentDrawerPosition.postValue(DrawerList.Empty)

      }
    }
  }

  fun createList(){
    viewModelScope.launch {
      val id=createList.invoke()
      toNewList.postValue(id)
      updateList()
      currentRvPosition.postValue(id)
      currentDrawerPosition.postValue(DrawerList.Empty)
    }

  }
  fun refreshList() {
    updateList()
  }
}