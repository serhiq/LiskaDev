package com.gmail.uia059466.liska.setting

import androidx.lifecycle.*
import com.gmail.uia059466.liska.addeditcatalog.units.GetAllFavsUnits
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class SettingGeneralViewModel(
  private val prefs: UserRepository,
  private val getAllFavsUnits: GetAllFavsUnits
) : ViewModel() {

    private val _snackbarText =
        SingleLiveEvent<String>()
    val snackbarText: LiveData<String> = _snackbarText

    val navigateToManualSortList = SingleLiveEvent<Boolean>()
    val navigateToManualCatalogSort = SingleLiveEvent<Boolean>()

    val sortOrderList: MutableLiveData<SortOrder> = MutableLiveData(prefs.readSortOrderList())
    val sortOrderCatalog: MutableLiveData<SortOrder> = MutableLiveData(prefs.readSortOrderCatalog())
    val favUnits: MutableLiveData<List<String>> = MutableLiveData(getAllFavsUnits.execute())


    fun takeAction(action: SettingGeneralAction) {
        when (action) {
          is SettingGeneralAction.SortList -> {
              sortOrderList.postValue(action.sort)
            prefs.saveSortOrderList(action.sort)
            if (action.sort == SortOrder.MANUAL_SORT) {
              viewModelScope.launch {
                navigateToManualSortList.postValue(true)
              }
            }
          }
            is SettingGeneralAction.SortCatalog -> sortCatalog(action.sort)
        }
    }

    private fun sortCatalog(sort: SortOrder) {
        sortOrderList.postValue(sort)
        prefs.saveSortOrderCatalog(sort)
        if (sort == SortOrder.MANUAL_SORT) {
            viewModelScope.launch {
                navigateToManualCatalogSort.postValue(true)
            }
        }
    }

    fun refreshFavsUnits() {
       favUnits.postValue(getAllFavsUnits.execute())
    }
}
