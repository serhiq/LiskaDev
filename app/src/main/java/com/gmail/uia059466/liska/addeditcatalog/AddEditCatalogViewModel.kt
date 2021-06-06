package com.gmail.uia059466.liska.addeditcatalog

import androidx.lifecycle.*
import com.gmail.uia059466.liska.addeditcatalog.units.GetSelectedUnit
import com.gmail.uia059466.liska.addeditcatalog.units.GetUnitsPanel
import com.gmail.uia059466.liska.addeditcatalog.units.SaveSelectedUnit
import com.gmail.uia059466.liska.addeditcatalog.units.UnitPanelState
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.domain.usecase.catalog.GetCatalog
import com.gmail.uia059466.liska.domain.usecase.catalog.UpdateCatalogItems
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class AddEditCatalogViewModel(
  private val getCatalog: GetCatalog,
  private val getPanel: GetUnitsPanel,
  private val getSelected: GetSelectedUnit,
  private val saveSelected: SaveSelectedUnit,
  private val updateItemsUsecase: UpdateCatalogItems,

  ) : ViewModel() {

  lateinit var editedItem: CatalogItem
  lateinit var baseCatalog:CatalogDatabase
  var indexEditedItem=0

  private val _catalogItem =  MutableLiveData<CatalogItem>()
  val  catalogItem: LiveData<CatalogItem> = _catalogItem


  private val _runBack =  SingleLiveEvent<Boolean>()
  val  runBack: LiveData<Boolean> = _runBack

  private val _panel =  MutableLiveData<UnitPanelState>()
  val  panel: LiveData<UnitPanelState> = _panel
  
  private var dataLoading=false
  fun start(id: Long, indexI: Int) {
    if (dataLoading) return
    
    viewModelScope.launch {
      val mumu = getCatalog.invoke(id)
      if (mumu!=null){
        baseCatalog=mumu
        indexEditedItem=indexI
      }
      val itemEdit=mumu?.list?.get(indexI)
      if (itemEdit!=null){
        editedItem=itemEdit
        saveSelected.execute(itemEdit.unit)
        _panel.postValue(getPanel.execute())
        updateItem()
        dataLoading=true
      }else{
        displayError()
      }}
  }

  private fun displayError() {
    _runBack.postValue(true)
  }


  private fun saveInRepository() {
    viewModelScope.launch {
      val s=editedItem
      val newItems=baseCatalog.list.toMutableList()
      newItems[indexEditedItem]=s
      updateItemsUsecase.invoke(baseCatalog.id,newItems)
      _runBack.postValue(true)
    }
  }
  
  fun unitSelected(unit:String){
    val s = editedItem
    editedItem=s.copy(unit=unit)
    saveSelected.execute(unit)
  }
  
  fun increaseQuantity(){
    editedItem.quantity++
    updateItem()
  }
  
  fun decreaseQuantity() {
    if (editedItem.quantity>0){
      editedItem.quantity--
    }
    updateItem()
  }
  
  fun saveItem(title: String, quantity: String) {
    val q=quantity.toIntOrNull()
    val new= editedItem.copy(title=title,quantity =q?:0)
    editedItem=new
    saveInRepository()
    
    _runBack.postValue(true)
  }
  
  private fun updateItem(){
    _catalogItem.postValue(editedItem)
  }

  var isNeedUpdate=false
  fun updateIfNeedUpdated() {
    if (isNeedUpdate){
      val m = getSelected.execute()
      val s = editedItem

      editedItem =s.copy(unit = m)
      updateItem()
      _panel.postValue(getPanel.execute())
      isNeedUpdate=false
    }
  }
}
