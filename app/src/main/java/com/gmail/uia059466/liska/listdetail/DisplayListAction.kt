package com.gmail.uia059466.liska.listdetail

import com.gmail.uia059466.liska.data.database.ListItem

sealed class DisplayListAction {
  
  class MoveToList(val adapterList: List<ListItem>, val id: Long, val selected: List<ListItem>) :
          DisplayListAction()
  
  class ChangeTitle(val title: String) : DisplayListAction()
  class ChangeMode(val mode: Int) : DisplayListAction()
  class ChangeTitleObject(val title: String) : DisplayListAction()
  
  class RunBack(val adapterList: List<ListItem>) : DisplayListAction()
  object displayObjectTitle : DisplayListAction()
  object displayEditTitle : DisplayListAction()
 
  object DeleteList : DisplayListAction()
  object DisplayDialogMoveSelected : DisplayListAction()
  class CopyList(val adapterList: List<ListItem>) : DisplayListAction()
  class NavigateToCatalog(val adapterList: List<ListItem>) : DisplayListAction()
  class NavigateToCatalogQuantity(val adapterList: List<ListItem>) : DisplayListAction()
  class SaveItems(val items: List<ListItem>) : DisplayListAction()
}