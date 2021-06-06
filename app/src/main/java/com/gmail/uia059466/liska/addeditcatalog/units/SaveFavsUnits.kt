package com.gmail.uia059466.liska.addeditcatalog.units

class SaveFavsUnits(
        private val unitsRepository: UnitsRepository
                     ) {
  
   fun execute(list:List<String>) = unitsRepository.saveFavorites(list)
}