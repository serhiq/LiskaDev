package com.gmail.uia059466.liska.lists.sortorder

import androidx.annotation.StringRes
import com.gmail.uia059466.liska.R

enum class SortOrder(val rawValue:String , @StringRes val title: Int) {
  A_Z("a-z", R.string.sort_alphabetical),
  NEWEST_FIRST("new_first", R.string.sort_date_added),
  LAST_MODIFIED("last", R.string.last_modificated),
  MANUAL_SORT("manual_sort", R.string.sort_by_human);

  companion object {
    fun fromString(raw: String): SortOrder {
      return SortOrder.values().single { it.rawValue == raw }
    }
    
    fun toString(value: SortOrder): String = value.rawValue
  }
}