package com.gmail.uia059466.liska.selectfromcatalog

import com.gmail.uia059466.liska.catalog.AddItemsInCatalog
import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.lists.ListsAction
import com.gmail.uia059466.liska.lists.sortorder.SortOrder

sealed class CatalogSelectAction  {
  object RunBack : CatalogSelectAction()
  class RunBackAndSave(val commands:List<AddItemsInCatalog>, val items:List<String>) : CatalogSelectAction()
  class ChangeModeToItemView(val title:String): CatalogSelectAction()
}