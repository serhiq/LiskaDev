package com.gmail.uia059466.liska.catalog

import androidx.lifecycle.*
import com.gmail.uia059466.liska.data.database.*
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.catalog.*
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatalogViewRootViewModel(

    private val getAll: GetAllCatalog,
    private val createCatalog: CreateCatalog,
    private val deleteCatalog: DeleteCatalog,
    private val updateItemsUsecase: UpdateCatalogItems,
    private val updateTitle: UpdateCatalogTitle,
    prefs: UserRepository

) : ViewModel() {
    var title = ""
    var currentIdCatalog=0L

    private val _runBack = SingleLiveEvent<Boolean>()
    val runBack: LiveData<Boolean> = _runBack

    private val rawList: MutableList<CatalogDatabaseEdit> = mutableListOf()

    val displayListAdapter = MutableLiveData<CatalogStateAdapter>()

    var _sortOrder: SortOrder =prefs.readSortOrderCatalog()
    init {
        updateList()
    }

    private fun updateList() {
        viewModelScope.launch {
            loadRawList()
            displayRootFolder()
        }
    }

    private fun displayRootFolder() {
        displayListAdapter.postValue(
          CatalogStateAdapter(
            mode = AdapterViewCatalogMode.RootFolder,
            currentId = 0,
            displayList = rawList.getFolders().toMutableList()
          )
        )
    }

    fun takeAction(action: CatalogViewAction) {
        return when (action) {
          is CatalogViewAction.RunBack -> runBack(action.items)
          CatalogViewAction.ChangeModeToEdit -> changeModeToEdit()
          is CatalogViewAction.ChangeModeToViewFolder -> displayFolder(action.id, action.newTitle)
          CatalogViewAction.ChangeModeToFolderRoot -> displayRootFolder()
          is CatalogViewAction.ChangeTitle -> title = action.title
          is CatalogViewAction.CreateCatalog -> createCatalog(action.title)
          is CatalogViewAction.DeleteCatalog -> deleteCatalog(action.id)
            is CatalogViewAction.ChangeTitleCatalog -> changeTitleObject(action.newTitle)
        }
    }

    private fun displayFolder(id: Long, newTitle: String) {
        title = newTitle
        currentIdCatalog=id
        displayListAdapter.postValue(
          CatalogStateAdapter(
            mode = AdapterViewCatalogMode.ItemsView,
            currentId = id,
            displayList = rawList.getItems(id).toMutableList()
          )
        )
    }

    private fun sortedTask(order: SortOrder, list: List<CatalogDatabaseEdit>): List<CatalogDatabaseEdit> {
        return when (order) {
            SortOrder.NEWEST_FIRST -> list.sortedByDescending { it.creationDate }
            SortOrder.A_Z -> list.sortedBy { it.title }
            SortOrder.LAST_MODIFIED -> list.sortedByDescending { it.lastModificationDate }
            SortOrder.MANUAL_SORT -> list.sortedBy { it.order }
        }
    }

    private suspend fun loadRawList() {
        val newList = getAll.invoke().toCatalogDatabaseEdit()
        val sorted=sortedTask(_sortOrder,newList)
        rawList.clear()
        rawList.addAll(sorted)
    }

    private fun deleteCatalog(id: Long) {
        viewModelScope.launch {
            deleteCatalog.invoke(id)
            loadRawList()
            displayRootFolder()
        }
    }

    private fun createCatalog(title: String) {
        viewModelScope.launch {
            createCatalog.invoke(title)
            loadRawList()
            displayListAdapter.postValue(
              CatalogStateAdapter(
                mode = AdapterViewCatalogMode.RootFolder,
                currentId = 0,
                displayList = rawList.getFolders().toMutableList()
              )
            )

        }
    }

    private fun changeModeToEdit() {
        val state = displayListAdapter.value
        if (state != null) {
            when (state.mode) {
              AdapterViewCatalogMode.ItemsView -> displayListAdapter.postValue(state.copy(mode = AdapterViewCatalogMode.ItemEdit))
            }
        }
    }

    private fun runBack(items: List<CatalogItem>?) {
        val state = displayListAdapter.value
        if (state != null) {
            when (state.mode) {
              AdapterViewCatalogMode.ItemEdit -> toItemView(items)
              AdapterViewCatalogMode.ItemsView -> displayRootFolder()
              AdapterViewCatalogMode.RootFolder -> naturalRunBack()
            }
        }
    }

    private fun toItemView(items: List<CatalogItem>?) {
        val isNeedForceUpdates = !items.isNullOrEmpty()

        if (isNeedForceUpdates) {
            updateItemsAndLoad(items)
        } else {
            val state = displayListAdapter.value
            if (state != null) {
                displayListAdapter.postValue(state.copy(mode = AdapterViewCatalogMode.ItemsView))
            }
        }
    }

    private fun updateItemsAndLoad(items: List<CatalogItem>?) {
        val state = displayListAdapter.value
        viewModelScope.launch {
            if (state != null && items != null) {

                val taskUpdate = async {
                    updateItemsUsecase.invoke(state.currentId, items)
                }
                val result = taskUpdate.await() // non ui thread, suspend until finished

                loadRawList()

                displayListAdapter.postValue(
                  CatalogStateAdapter(
                    mode = AdapterViewCatalogMode.ItemsView,
                    currentId = state.currentId,
                    displayList = rawList.getItems(state.currentId).toMutableList()
                  )
                )
            }

        }
    }

    private fun naturalRunBack() {
        _runBack.postValue(true)
    }

    fun applyManualsortAndUpdate() {

        updateList()
    }

    private fun changeTitleObject(newTitle: String) {
        title=newTitle
        viewModelScope.launch {
            updateTitle.invoke(currentIdCatalog,newTitle)
                loadRawList()
         }
    }

    fun refreshList() {
        viewModelScope.launch {
            loadRawList()
            val oldV=displayListAdapter.value?.currentId?:0
            displayFolder(oldV,title)
        }
    }
}