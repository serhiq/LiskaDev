package com.gmail.uia059466.liska.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.addeditcatalog.AddEditCatalogViewModel
import com.gmail.uia059466.liska.catalog.CatalogViewRootViewModel
import com.gmail.uia059466.liska.catalog.manualsortlist.ManualSortCatalogViewModel
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.domain.usecase.MessageRepositoryImpl
import com.gmail.uia059466.liska.listdetail.DisplayListViewModel
import com.gmail.uia059466.liska.lists.ListsViewModel
import com.gmail.uia059466.liska.main.MainActivityModel
import com.gmail.uia059466.liska.manualsortlist.ManualSortViewModel
import com.gmail.uia059466.liska.selectfromcatalog.AddItemsCatalogUseCase
import com.gmail.uia059466.liska.selectfromcatalog.AddItemsUneaseImpl
import com.gmail.uia059466.liska.selectfromcatalog.FufuCatalogSelectViewModel
import com.gmail.uia059466.liska.selectunit.SelectUnitsViewModel
import com.gmail.uia059466.liska.warehouse.WarehouseViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val a: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainActivityModel::class.java) ->
                    MainActivityModel(
                        getAllListUseCases = InjectorUtils.getAllListUseCases(a),
                        createList = InjectorUtils.createListUseCase(a),
                        prefs = InjectorUtils.prefs(a)
                    )

                isAssignableFrom(SelectUnitsViewModel::class.java) ->
                    SelectUnitsViewModel(
                        getAllUnits = InjectorUtils.getAllUnits(a),
                        getAllFavsUnits = InjectorUtils.getAllFavsUnits(a),
                        saveFavorites = InjectorUtils.saveFavorites(a),
                        saveUnitsUseCase = InjectorUtils.saveUnits(a),
                        saveSelectedUnit = InjectorUtils.saveSelected(a)

                    )

                isAssignableFrom(ManualSortCatalogViewModel::class.java) ->
                    ManualSortCatalogViewModel(
                        getAll = InjectorUtils.getAllCatalog(a),
                        saveNewOrderList = InjectorUtils.saveNewOrderCatalogUseCases(a),
                        prefs = InjectorUtils.prefs(a)
                    )

                isAssignableFrom(AddEditCatalogViewModel::class.java) ->
                    AddEditCatalogViewModel(
                        getCatalog = InjectorUtils.getCatalog(a),
                        getPanel = InjectorUtils.getPanel(a),
                        getSelected = InjectorUtils.getSelected(a),
                        saveSelected = InjectorUtils.saveSelected(a),
                        updateItemsUsecase = InjectorUtils.updateItemsCatalogUseCases(a)
                    )

                isAssignableFrom(ListsViewModel::class.java) ->
                    ListsViewModel(
                        getAllListUseCases = InjectorUtils.getAllListUseCases(a),
                        saveNewOrderList = InjectorUtils.saveNewOrderList(a),
                        createList = InjectorUtils.createListUseCase(a),
                        prefs = InjectorUtils.prefs(a),
                        messageRepository = MessageRepositoryImpl.getInstance()
                    )
                isAssignableFrom(CatalogViewRootViewModel::class.java) ->
                    CatalogViewRootViewModel(
                        getAll = InjectorUtils.getAllCatalog(a),
                        createCatalog = InjectorUtils.createCatalog(a),
                        deleteCatalog = InjectorUtils.deleteCatalogUseCases(a),
                        updateItemsUsecase = InjectorUtils.updateItemsCatalogUseCases(a),
                        prefs = InjectorUtils.prefs(a),
                        updateTitle = InjectorUtils.updateCatalogTitle(a)

                    )

                isAssignableFrom(ManualSortViewModel::class.java) ->
                    ManualSortViewModel(
                        getAllListUseCases = InjectorUtils.getAllListUseCases(a),
                        saveNewOrderList = InjectorUtils.saveNewOrderList(a),
                        prefs = InjectorUtils.prefs(a)
                    )
                isAssignableFrom(DisplayListViewModel::class.java) ->
                    DisplayListViewModel(
                        getList = InjectorUtils.getListUseCases(a),
                        deleteList = InjectorUtils.deleteListUseCases(a),
                        deleteEmptyList =InjectorUtils.deleteEmptyListUseCases(a),
                        copyList = InjectorUtils.copyListUseCases(a),
                        getAllList = InjectorUtils.getAllListUseCases(a),
                        addToList = InjectorUtils.addToListUseCase(a),
                        updateItems = InjectorUtils.updateItemsListUseCases(a),
                        updateTitleListUseCases = InjectorUtils.updateTitleListUseCases(a),
                        prefs = UserPreferencesRepositoryImpl.getInstance(a)
                    )

                isAssignableFrom(FufuCatalogSelectViewModel::class.java) ->
                    FufuCatalogSelectViewModel(
                        getAll = InjectorUtils.getAllCatalog(a), prefs = InjectorUtils.prefs(a),
                        addItemsCatalog = AddItemsCatalogUseCase(
                            (a as LiskaApplication).catalogRepository
                        ),
                        addItemsInList = AddItemsUneaseImpl(
                            repository = a.listRepository
                        ),)
                isAssignableFrom(WarehouseViewModel::class.java) ->
                    WarehouseViewModel(
                        warehouseRepository =(a as LiskaApplication).wareHouseRepository ,messageRepository = MessageRepositoryImpl.getInstance(), prefs = InjectorUtils.prefs(a))
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}