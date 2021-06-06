package com.gmail.uia059466.liska.catalog

import com.gmail.uia059466.liska.data.database.CatalogItem


sealed class CatalogViewAction  {
  class RunBack(val items: List<CatalogItem>?=null) : CatalogViewAction()
  object ChangeModeToEdit  : CatalogViewAction()
  class ChangeModeToViewFolder(val id: Long, val newTitle: String) : CatalogViewAction()
  object ChangeModeToFolderRoot:CatalogViewAction()
  class ChangeTitle(val title: String) : CatalogViewAction()
  class CreateCatalog(val title: String) : CatalogViewAction()
  class DeleteCatalog(val id:Long) : CatalogViewAction()
  class ChangeTitleCatalog(val newTitle: String) : CatalogViewAction()
}