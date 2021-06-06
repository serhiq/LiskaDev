package com.gmail.uia059466.liska.setting.selectcatalog

enum class SelectCatalogOption(val rawValue: String) {
  CHECKBOX("checkbox"),
  SELECT("select"),
  TWO_VAR("two_var");
  
  companion object {
    fun fromString(raw: String): SelectCatalogOption {
      return SelectCatalogOption.values().single { it.rawValue == raw }
    }
    
    fun toString(value: SelectCatalogOption): String = value.rawValue
  }
}
