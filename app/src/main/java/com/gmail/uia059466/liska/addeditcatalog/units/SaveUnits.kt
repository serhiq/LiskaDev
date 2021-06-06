package com.gmail.uia059466.liska.addeditcatalog.units

class SaveUnits(
        private val unitsRepository: UnitsRepository
                     ) {
  
   fun execute(list:List<String>) = unitsRepository.saveUnits(list)
}