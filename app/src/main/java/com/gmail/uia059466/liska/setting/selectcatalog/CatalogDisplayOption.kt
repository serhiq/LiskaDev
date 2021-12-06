package com.gmail.uia059466.liska.setting.selectcatalog

import androidx.annotation.StringRes
import com.gmail.uia059466.liska.R

enum class CatalogDisplayOption(val rawValue: String, @StringRes val title: Int) {
  CHECKBOX("checkbox", R.string.dialog_select),
  SELECT("select",R.string.dialog_quantity),
  TWO_VAR("two_var",R.string.dialog_two);
  
  companion object {
    fun fromString(raw: String): CatalogDisplayOption {
      return values().single { it.rawValue == raw }
    }
    
    fun toString(value: CatalogDisplayOption): String = value.rawValue
  }
}
