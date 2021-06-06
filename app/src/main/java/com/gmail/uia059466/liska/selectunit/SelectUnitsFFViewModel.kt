package com.gmail.uia059466.liska.selectunit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uia059466.liska.addeditcatalog.units.*
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class SelectUnitsFFViewModel(
    getAllUnits: GetAllUnits,
    getAllFavsUnits: GetAllFavsUnits,
    private val saveSelectedUnit: SaveSelectedUnit,
    private val saveUnitsUseCase: SaveUnits,
    private val saveFavorites: SaveFavsUnits
) : ViewModel() {

    private val _adapterMode = SingleLiveEvent<Int>()
    val adapterMode: LiveData<Int> = _adapterMode
    var firstMode = 0


    private val _runBack = SingleLiveEvent<Boolean>()
    val runBack: LiveData<Boolean> = _runBack


    val stateAdapter = SelectUnitsAdapterState(
        data = getAllUnits.execute(),
        fav = getAllFavsUnits.execute()
    )

    fun start(mode: Int) {
        _adapterMode.postValue(mode)
        firstMode = mode
    }

    private fun saveUnits(list: List<String>) {
        saveUnitsUseCase.execute(list)
    }

    fun runBack(state: SelectUnitsAdapterState) {
        when (firstMode == SelectAdapter.MODE_SELECT) {
            true -> runBackSelect(state)
            else -> runBackFavorites(state)
        }
    }

    private fun runBackFavorites(state: SelectUnitsAdapterState) {
        when (_adapterMode.value) {
            SelectAdapter.MODE_EDIT -> {
                _adapterMode.postValue(SelectAdapter.MODE_FAVORITES)
            }
            SelectAdapter.MODE_FAVORITES -> {
                naturalRunBack(state)
            }
        }
    }

    private fun runBackSelect(state: SelectUnitsAdapterState) {
        when (_adapterMode.value) {
            SelectAdapter.MODE_SELECT -> naturalRunBack(state)
            SelectAdapter.MODE_EDIT -> {
                _adapterMode.postValue(SelectAdapter.MODE_SELECT)
            }
            SelectAdapter.MODE_FAVORITES -> {
                _adapterMode.postValue(SelectAdapter.MODE_SELECT)
            }
        }
    }

    private fun naturalRunBack(state: SelectUnitsAdapterState) {
        viewModelScope.launch {
            saveUnits(state.data)
            saveFavorites(state.fav)
        }
        _runBack.postValue(true)
    }

    fun enableEdit() {
        _adapterMode.postValue(SelectAdapter.MODE_EDIT)
    }

    fun enableFavoritesSelect() {
        _adapterMode.postValue(SelectAdapter.MODE_FAVORITES)
    }

    private fun saveFavorites(fav: List<String>) {
        saveFavorites.execute(fav)
    }

    fun saveSelected(selected: String) {
        saveSelectedUnit.execute(selected)
        _runBack.postValue(true)
    }
}

