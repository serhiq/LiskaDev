package com.gmail.uia059466.liska.setting.themes

enum class Theme(val rawValue: String) {
  BLUE("blue"),
  RED("dark"),
  MINT("mint"),
  GRAY("gray");
  
  companion object {
    fun fromString(raw: String): Theme {
      return Theme.values().single { it.rawValue == raw }
    }
    
    fun toString(value: Theme): String = value.rawValue
  }
}
