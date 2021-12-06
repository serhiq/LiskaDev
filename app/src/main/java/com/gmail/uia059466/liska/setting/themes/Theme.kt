package com.gmail.uia059466.liska.setting.themes

import androidx.annotation.StringRes
import com.gmail.uia059466.liska.R

enum class Theme(val rawValue: String, @StringRes val title: Int) {
  BLUE("blue", R.string.theme_light),
  RED("dark", R.string.theme_dark),
  MINT("mint",R.string.theme_mint),
  GRAY("gray",R.string.theme_gray);

  companion object {
    fun fromString(raw: String): Theme {
      return values().single { it.rawValue == raw }
    }
    
    fun toString(value: Theme): String = value.rawValue
  }
}
