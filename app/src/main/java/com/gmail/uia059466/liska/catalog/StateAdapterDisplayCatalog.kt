package com.gmail.uia059466.liska.catalog

import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.data.database.CatalogDatabaseEdit
import com.gmail.uia059466.liska.data.database.CatalogItem


fun List<CatalogDatabaseEdit>.getFolders(): List<DisplayCatalog.FolderDisplay> {
  return this.map {
    DisplayCatalog.FolderDisplay(id = it.id, title = it.title)
  }
}


fun List<CatalogDatabaseEdit>.getItems(id: Long): List<DisplayCatalog.ItemDisplay> {
  
  val catalog = this.find { it.id == id }
  
  return catalog?.list?.map {
      DisplayCatalog.ItemDisplay(
              id = catalog.id,
              title = it.title,
              quantity = it.quantity,
              unit = it.unit
                                )
  }
    ?: emptyList<DisplayCatalog.ItemDisplay>()
}

fun List<CatalogDatabaseEdit>.getItems(): List<DisplayCatalog.ItemDisplay> {
  
  val allList = this.flatMap { it.list }
  return allList.map {
    DisplayCatalog.ItemDisplay(
            id = 0,
            title = it.title,
            quantity = it.quantity,
            unit = it.unit
                              )
  }
}

fun DisplayCatalog.ItemDisplay.toCatalogItem(): CatalogItem {
  return CatalogItem(
          title = this.title,
          quantity = this.quantity,
          unit = this.unit,
          isSelected = false
                    )
  
}

sealed class DisplayCatalog(open val id: Long) {
  class FolderDisplay(
          override val id: Long,
          val title: String
                     ) : DisplayCatalog(id = id)
  
  class ItemDisplay(
          override val id: Long,
          val title: String,
          val quantity: Int,
          val unit: String,
          var isSelected: Boolean = false
                   ) : DisplayCatalog(id = id)
  
  
  class CreateItemDisplay(
          override val id: Long,
          val title: String,
          val quantity: Int,
          val unit: String,
          var isSelected: Boolean = false
                         ) : DisplayCatalog(id = id)
}

fun List<DisplayCatalog.FolderDisplay>.toDatabaseForOrder(): List<CatalogDatabase> {
  return this.map {
    CatalogDatabase(
            id = it.id,
            title = it.title,
            order = 0,
            list = listOf(),
            lastModificationDate = 0,
            creationDate = 0
                   )
  }
}

fun List<DisplayCatalog>.requestSelected(): List<CatalogItem> {
  val list = this.filterIsInstance<DisplayCatalog.ItemDisplay>()
    return list.map {
    CatalogItem(
            title = it.title,
            quantity = it.quantity,
            unit = it.unit,
            isSelected = false
               )
  }
}


fun List<CatalogDatabase>.asFolders(): List<DisplayCatalog.FolderDisplay> {
  return this.map {
    DisplayCatalog.FolderDisplay(id = it.id, title = it.title)
  }
}