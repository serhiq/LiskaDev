package com.gmail.uia059466.liska.data.database

import androidx.room.TypeConverter

internal object DisplayItemTypeConverter {
  
  @TypeConverter
  fun stringToDisplayItemList(data: String): List<ListItem> {
    if (data.isBlank()) {
      return emptyList()
    }
  
    val converter = DisplayItemConverter()
    return converter.fromStringList(data)
  }
  
  @TypeConverter
  fun displayItemListToString(lists: List<ListItem>): String {
    val converter = DisplayItemConverter()
    return converter.toStringList(lists)
  }
}