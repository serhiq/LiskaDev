package com.gmail.uia059466.liska.listdetail

import androidx.lifecycle.*
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.data.database.ListItem
import com.gmail.uia059466.liska.data.database.listIsEquals
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.domain.usecase.*
import com.gmail.uia059466.liska.listdetail.DisplayListAction.*
import com.gmail.uia059466.liska.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class DisplayListViewModel(
  private val getList:GetListUseCases,
  private val deleteList: DeleteListUseCases,
  private val deleteEmptyList:DeleteEmptyListUseCases,
  private val copyList: CopyListUseCase,
  private val updateTitleListUseCases: UpdateListTitle,
  private val updateItems: UpdateItemsListUsecase,
  private val getAllList: GetAllListUseCase,
  private val addToList: AddToList,
  private val prefs: UserPreferencesRepositoryImpl,

                                  ) : ViewModel() {

  private val _title =  SingleLiveEvent<String>()
  val  title: LiveData<String> = _title

    private val _adapterMode = SingleLiveEvent<Int>()
  val  adapterMode: LiveData<Int> = _adapterMode

  private val _runBack =  SingleLiveEvent<Boolean>()
  val  runBack: LiveData<Boolean> = _runBack

  private val _navigateToCatalog =  SingleLiveEvent<Long>()
  val  navigateToCatalog: LiveData<Long> = _navigateToCatalog

  private val _navigateToCatalogWithQuantity =  SingleLiveEvent<Long>()
  val  navigateToCatalogWithQuantity: LiveData<Long> = _navigateToCatalogWithQuantity

  private val _listsForDialog = SingleLiveEvent<List<ListDisplay>>()
  val listForMoveDialog: LiveData<List<ListDisplay>> = _listsForDialog

  private val _snackbarText = SingleLiveEvent<Int>()
  val snackbarText: LiveData<Int> = _snackbarText


  var idList:Long=0L
  var isEditModeM:Boolean=false

  private val _dataLoading = MutableLiveData<Boolean>()
  val dataLoading: LiveData<Boolean> = _dataLoading

  val refreshNavigation = SingleLiveEvent<Boolean>()

  var isNew:Boolean=false

  private val copyListItems= mutableListOf<ListItem>()

  var copyTitle=""

  fun requestItems():List<ListItem>{
    val listik= mutableListOf<ListItem>()
    for (item in copyListItems){
      listik.add(ListItem(title = item.title,isChecked = item.isChecked))
    }
    return  listik
  }

  private fun compareAndSave(fromAdapter: List<ListItem>){
    val isEqualList=listIsEquals(copyListItems,fromAdapter)
    if (!isEqualList){
      viewModelScope.launch {
        updateItems.invoke(idList,fromAdapter)
        copyListItems.clear()
        copyListItems.addAll(fromAdapter)
      }
    }
  }

  fun start(isEditMode: Boolean?, id: Long?,isNewFromBundle: Boolean) {
    if (isEditMode == null || id == null) {
      displayError()
    } else {
      isNew=isNewFromBundle
      prefs.lastId=id
      isEditModeM = isEditMode
      loadData(id, isEditMode)
    }
  }

  private fun loadData(id: Long, isEditMode: Boolean) {
    viewModelScope.launch {
      val result = getList.invoke(id)
      when(result){
        is HolderResult.Success -> displayLoadingResult(result.data, isEditMode)
        is HolderResult.Error -> displayError()
      }
    }
  }

  private fun displayLoadingResult(result: ListEdit?, isEditMode: Boolean) {
    if (result==null){
      displayError()
    }else{
      copyListItems.clear()
      copyListItems.addAll(result.data)
      copyTitle=result.title

      idList=result.id
      _dataLoading.value=true
      when(isEditMode){
        true->_adapterMode.value=DisplayListAdapter.EDIT
        false->_adapterMode.value=DisplayListAdapter.VIEW
      }
    }
  }

  private fun displayError() {
   _snackbarText.postValue(R.string.error_load)
    _runBack.postValue(true)
  }

  fun takeAction(action: DisplayListAction) {
   return when (action) {
      is ChangeMode -> changeMode(action.mode)
      is ChangeTitle            -> changeTitle(action.title)
      is ChangeTitleObject      -> changeTitleObject(action.title)
      is RunBack -> runBack(action)
      displayObjectTitle ->     _title.postValue(copyTitle)
      displayEditTitle          -> _title.postValue("${copyTitle}*")
      DeleteList ->             deleteList()
      is DisplayListAction.CopyList -> copyList(action)

      is NavigateToCatalog         ->  navigateToCatalogSelected(action)
      is NavigateToCatalogQuantity -> navigateToQuantity(action)


      is MoveToList             -> moveToList(action)
      DisplayDialogMoveSelected -> loadListDisplay()
     is SaveItems -> compareAndSave(action.items)
   }
  }

  private fun navigateToCatalogSelected(action: NavigateToCatalog) {
    viewModelScope.launch {
      compareAndSave(action.adapterList)
      _navigateToCatalog.postValue(idList)
    }
  }

  private fun navigateToQuantity(action: NavigateToCatalogQuantity) {
    viewModelScope.launch {
      compareAndSave(action.adapterList)
      _navigateToCatalogWithQuantity.postValue(idList)
    }
  }

  private fun moveToList(action: MoveToList) {
    viewModelScope.launch {
      compareAndSave(action.adapterList)
      addToList.invoke(action.id, action.selected)
    }
  }





  private fun loadListDisplay() {
    viewModelScope.launch {
      val mumu = getAllList.invoke()
      val posted=mumu.filter { it.id!=idList }
      if (posted.isEmpty()){
        _snackbarText.postValue(R.string.error_one_list)
      }else{
        _listsForDialog.postValue(posted)
      }
    }
  }

  private fun copyList(action:DisplayListAction.CopyList) {
    viewModelScope.launch {
      compareAndSave(action.adapterList)
      copyList.invoke(idList)
      refreshNavigation.postValue(true)
      _runBack.postValue(true)
    }
  }

  private fun saveItems(list: List<ListItem>) {
    viewModelScope.launch {
      updateItems.invoke(idList,list)
    }
  }

  private fun changeTitleObject(newTitle: String) {
    copyTitle=newTitle
    _title.postValue("${copyTitle}*")
    viewModelScope.launch {
      updateTitleListUseCases.invoke(idList,newTitle)
    }
    refreshNavigation.postValue(true)
  }

  private fun changeMode(mode: Int) {
    isEditModeM=true
    _adapterMode.value=mode
  }

  private fun changeTitle(title: String) {
    _title.postValue(title)
  }

  private fun deleteList() {
      viewModelScope.launch {
        deleteList.invoke(idList)
        refreshNavigation.postValue(true)
        _runBack.postValue(true)
      }
  }

  private fun runBack(action:RunBack) {
    compareAndSave(action.adapterList)

    when(_adapterMode.value){
      DisplayListAdapter.SELECT -> _adapterMode.postValue(DisplayListAdapter.EDIT)
      DisplayListAdapter.EDIT  -> changeToEdit()
      DisplayListAdapter.VIEW  -> runBackFromViewMode()
    }
  }

  private fun changeToEdit(){
    _adapterMode.postValue(DisplayListAdapter.VIEW)
  }

  private fun runBackFromViewMode()
  {
      _runBack.postValue(true)
  }
}