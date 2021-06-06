package com.gmail.uia059466.liska.selectfromcatalog

import androidx.lifecycle.*
import com.gmail.uia059466.liska.catalog.AddItemsInCatalog
import com.gmail.uia059466.liska.data.database.*
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.catalog.GetAllCatalog
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class FufuCatalogSelectViewModel(

    private val getAll: GetAllCatalog,
    private val addItemsCatalog: AddItemsCatalogUseCase,
    private val addItemsInList:AddItemsUneaseImpl,
    private val prefs: UserRepository

) : ViewModel() {

    var title = ""
    var idListingFor = 0L

    private val _runBack = SingleLiveEvent<Boolean>()
    val runBack: LiveData<Boolean> = _runBack

    val rawList: MutableList<CatalogDatabase> = mutableListOf()

    private val _dataLoading = SingleLiveEvent<Boolean>()
    val dataLoading: MutableLiveData<Boolean> = _dataLoading

    val isRootDisplayFolder =prefs.readIsDisplayFolder()

    val modeAdapter = MutableLiveData<AdapterCatalogMode>()

    val _sortOrder: SortOrder =prefs.readSortOrderCatalog()

    private val _snackbarText = SingleLiveEvent<String>()
    val snackbarText: LiveData<String> = _snackbarText

    init {
        updateList()
    }

    private fun updateList() {
        viewModelScope.launch {
            loadRawList()
        }
    }

    fun takeAction(action: CatalogSelectAction) {
        return when (action) {
          is CatalogSelectAction.RunBack -> runBack()
            is CatalogSelectAction.ChangeModeToItemView ->changeModeToItemView(action.title)
            is CatalogSelectAction.RunBackAndSave -> runBackAndSave(action.commands,action.items)
        }
    }

    private fun runBackAndSave(commands: List<AddItemsInCatalog>, items: List<String>) {
        viewModelScope.launch {

            if (commands.isNotEmpty()){
                addItemsCatalog.invoke(commands)
            }
            if (items.isNotEmpty()){
                addItemsInList.invoke(idListingFor,items)
            }
            _runBack.postValue(true)
        }
    }


    private fun changeModeToItemView(newTitle: String) {
        title=newTitle
        modeAdapter.postValue(AdapterCatalogMode.ItemsView)
    }

    private fun sortedTask(order: SortOrder, list: List<CatalogDatabase>): List<CatalogDatabase> {
        return when (order) {
            SortOrder.NEWEST_FIRST -> list.sortedByDescending { it.creationDate }
            SortOrder.A_Z -> list.sortedBy { it.title }
            SortOrder.LAST_MODIFIED -> list.sortedByDescending { it.lastModificationDate }
            SortOrder.MANUAL_SORT -> list.sortedBy { it.order }
        }
    }

    private suspend fun loadRawList() {
        val newList = getAll.invoke()
        val sorted=sortedTask(_sortOrder,newList)
        rawList.clear()
        rawList.addAll(sorted)
        dataLoading.postValue(true)
        toRoot()
    }

    private fun runBack() {
        val mode = modeAdapter.value
        if (mode != null) {
            when (mode) {
                AdapterCatalogMode.ItemsView -> toRoot()
                AdapterCatalogMode.RootFolder -> naturalRunBack()
            }
        }
    }

    private fun toRoot() {
        if (isRootDisplayFolder) {
            modeAdapter.postValue(AdapterCatalogMode.RootFolder)
        } else {
//            modeAdapter.postValue(AdapterFUFUSelectMode.DisplayAll)
        }

    }

    private fun naturalRunBack() {
        _runBack.postValue(true)
    }

    fun start(listId: Long) {
        idListingFor=listId
    }

    fun resetModelToView() {
        val mode=modeAdapter.value
        if (mode!=null){
            modeAdapter.postValue(mode)
        }
    }
}