package com.gmail.uia059466.liska.utils

import android.app.Application
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.addeditcatalog.units.*
import com.gmail.uia059466.liska.catalog.manualsortlist.SaveNewOrderCatalog
import com.gmail.uia059466.liska.catalog.manualsortlist.SaveNewOrderCatalogImpl
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.*
import com.gmail.uia059466.liska.domain.usecase.catalog.*

object InjectorUtils {
    fun provideViewModelFactory(application: Application): ViewModelFactory {
        return ViewModelFactory(application)
    }

    private fun listRepository(a:Application):ListRepository{
        return (a as LiskaApplication).listRepository
    }
    private fun messageRepository(a:Application): MessageRepository {
        return (a as LiskaApplication).messageRepository
    }

    fun getAllListUseCases(a: Application): GetAllListUseCase {
        return GetAllListUseCaseImpl(listRepository(a))
    }
    fun saveNewOrderList(a: Application): UpdateOrderInList {
        return UpdateOrderInListImpl(listRepository(a))
    }
//
    fun addToListUseCase(a: Application): AddToList {
        return AddToListImpl(listRepository(a),messageRepository(a))
    }

    fun copyListUseCases(a: Application): CopyListUseCase {
        return CopyList(listRepository(a),messageRepository(a))
    }

    fun createListUseCase(a: Application): InsertList {
        return InsertList((a as LiskaApplication).listRepository)
    }

    fun deleteListUseCases(a: Application): DeleteListUseCases {
        return DeleteListUseCasesImpl(listRepository(a),messageRepository(a))
    }
    fun deleteEmptyListUseCases(a: Application): DeleteEmptyListUseCases {
        return DeleteEmptyListUseCases(listRepository(a),messageRepository(a))
    }
    fun getListUseCases(a: Application): GetListUseCases {
        return GetListUseCasesImpl((a as LiskaApplication).listRepository)
    }

    fun updateListUseCases(a: Application): UpdateList {
        return UpdateListImpl((a as LiskaApplication).listRepository,messageRepository(a))
    }

    fun updateTitleListUseCases(a: Application): UpdateListTitle {
        return UpdateListTitleImpl((a as LiskaApplication).listRepository)
    }

    fun updateItemsListUseCases(a: Application): UpdateItemsListUsecase {
        return UpdateItemsInList((a as LiskaApplication).listRepository)
    }

    fun updateItemsCatalogUseCases(a: Application): UpdateCatalogItems {
        return UpdateCatalogItemsImpl((a as LiskaApplication).catalogRepository)
    }
    fun updateCatalogTitle(a: Application): UpdateCatalogTitle {
        return UpdateCatalogTitle((a as LiskaApplication).catalogRepository)
    }

    fun getAllCatalog(a: Application): GetAllCatalog {
        return GetAllCatalogImpl((a as LiskaApplication).catalogRepository)
    }

    fun createCatalog(a: Application): CreateCatalog {
        return CreateCatalog((a as LiskaApplication).catalogRepository)
    }

    fun deleteCatalogUseCases(a: Application): DeleteCatalog {
        return DeleteCatalogImpl((a as LiskaApplication).catalogRepository)
    }

    fun getCatalog(a: Application): GetCatalog {
        return GetCatalogImpl((a as LiskaApplication).catalogRepository)
    }

    fun getPanel(a: Application): GetUnitsPanel {
        return GetUnitsPanel(unitsRepository(a))
    }

   fun getSelected(a: Application): GetSelectedUnit {
        return GetSelectedUnit(unitsRepository(a))
    }

    fun saveSelected(a: Application): SaveSelectedUnit {
        return SaveSelectedUnit(unitsRepository(a))
    }

    fun getAllUnits(a: Application): GetAllUnits {
        return GetAllUnits(unitsRepository(a))
    }

    fun getAllFavsUnits(a: Application): GetAllFavsUnits {
        return GetAllFavsUnits(unitsRepository(a))
    }


    fun saveUnits(a: Application): SaveUnits {
        return SaveUnits(unitsRepository(a))
    }

    fun saveFavorites(a: Application): SaveFavsUnits {
        return SaveFavsUnits(unitsRepository(a))
    }


    fun saveNewOrderCatalogUseCases(a: Application): SaveNewOrderCatalog {
        return SaveNewOrderCatalogImpl((a as LiskaApplication).catalogRepository)
    }



    fun prefs(a: Application): UserRepository {
        return UserPreferencesRepositoryImpl.getInstance(a)
    }
    fun unitsRepository(a: Application): UnitsRepository {
        return UserPreferencesRepositoryImpl.getInstance(a)
    }

}