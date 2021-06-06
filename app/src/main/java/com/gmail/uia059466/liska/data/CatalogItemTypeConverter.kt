package com.gmail.uia059466.liska.data

import androidx.room.TypeConverter
import com.gmail.uia059466.liska.data.database.CatalogItem

internal object CatalogItemTypeConverter {
  
  @TypeConverter
  fun stringToDisplayItemList(data: String): List<CatalogItem> {
    if (data.isBlank()) {
      return emptyList()
    }
  
    val converter = CatalogItemConverter()
    return converter.fromStringList(data)
  }
  
  @TypeConverter
  fun displayItemListToString(lists: List<CatalogItem>): String {
    val converter = CatalogItemConverter()
    return converter.toStringList(lists)
  }
}