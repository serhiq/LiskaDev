package com.gmail.uia059466.liska.setting

class FavoriteUnitsFormatter {
    fun createDescription(units: List<String>): String {
      return  when{
          units.isNotEmpty() -> units.joinToString(separator = ",")
          else -> "не выбраны"
      }
    }
}
