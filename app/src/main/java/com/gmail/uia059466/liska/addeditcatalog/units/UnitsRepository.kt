package com.gmail.uia059466.liska.addeditcatalog.units

interface UnitsRepository {
  fun units():List<String>
  fun favorites():List<String>
  fun selected():String
  
  fun saveUnits(list:List<String>)
  fun saveFavorites(list: List<String>)
  fun saveSelected(selected: String)
}