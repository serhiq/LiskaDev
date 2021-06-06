package com.gmail.uia059466.liska.lists.sortorder

enum class SortOrder(val rawValue:String) {
  A_Z("a-z"),
  NEWEST_FIRST("new_first"),
  LAST_MODIFIED("last"),
  MANUAL_SORT("manual_sort");

  companion object {
    fun fromString(raw: String): SortOrder {
      return SortOrder.values().single { it.rawValue == raw }
    }
    
    fun toString(value: SortOrder): String = value.rawValue
  }
}