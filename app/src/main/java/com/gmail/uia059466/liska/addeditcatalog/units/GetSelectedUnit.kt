package com.gmail.uia059466.liska.addeditcatalog.units

class GetSelectedUnit(
        private val unitsRepository: UnitsRepository
                     ) {
  
   fun execute() = unitsRepository.selected()
}