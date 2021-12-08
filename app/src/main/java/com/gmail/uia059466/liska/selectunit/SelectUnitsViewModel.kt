package com.gmail.uia059466.liska.selectunit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gmail.uia059466.liska.addeditcatalog.units.*
import com.gmail.uia059466.liska.utils.SingleLiveEvent

class SelectUnitsViewModel(
    getAllUnits: GetAllUnits,
    getAllFavsUnits: GetAllFavsUnits,
    private val saveSelectedUnit: SaveSelectedUnit,
    private val saveUnitsUseCase: SaveUnits,
    private val saveFavorites: SaveFavsUnits
) : ViewModel() {

    private val _adapterMode = SingleLiveEvent<UnitsAdapter.Mode>()
    val adapterMode: LiveData<UnitsAdapter.Mode> = _adapterMode

    private var firstMode = UnitsAdapter.Mode.SELECT

    private val _runBack = SingleLiveEvent<Boolean>()
    val runBack: LiveData<Boolean> = _runBack

    fun start(mode: UnitsAdapter.Mode) {
        _adapterMode.postValue(mode)
        firstMode = mode
    }

    private fun saveUnits(list: List<String>) {
        saveUnitsUseCase.execute(list)
    }

    fun runBack(state: SelectUnitsAdapterState) {
        when (firstMode == UnitsAdapter.Mode.SELECT) {
            true -> runBackSelect(state)
            else -> runBackFavorites(state)
        }
    }

    private fun runBackFavorites(state: SelectUnitsAdapterState) {
        when (_adapterMode.value) {
            UnitsAdapter.Mode.EDIT -> _adapterMode.postValue(UnitsAdapter.Mode.FAVORITES)
            UnitsAdapter.Mode.FAVORITES -> naturalRunBack(state)
        }
    }

    private fun runBackSelect(state: SelectUnitsAdapterState) {
        when (_adapterMode.value) {
            UnitsAdapter.Mode.EDIT -> _adapterMode.postValue(UnitsAdapter.Mode.SELECT)
            UnitsAdapter.Mode.FAVORITES ->_adapterMode.postValue(UnitsAdapter.Mode.SELECT)
            UnitsAdapter.Mode.SELECT -> naturalRunBack(state)
        }
    }

    private fun naturalRunBack(state: SelectUnitsAdapterState) {
            saveUnits(state.data)
            saveFavorites(state.fav)
           _runBack.postValue(true)
    }

    fun enableEdit() {
        _adapterMode.postValue(UnitsAdapter.Mode.EDIT)
    }

    fun enableFavoritesSelect() {
        _adapterMode.postValue(UnitsAdapter.Mode.FAVORITES)
    }

    private fun saveFavorites(fav: List<String>) {
        saveFavorites.execute(fav)
    }

    fun saveSelected(selected: String) {
        saveSelectedUnit.execute(selected)
        _runBack.postValue(true)
    }

    val stateAdapter = SelectUnitsAdapterState(
        data = getAllUnits.execute(),
        fav = getAllFavsUnits.execute()
    )
}

