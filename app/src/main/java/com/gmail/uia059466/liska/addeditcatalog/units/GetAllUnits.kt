package com.gmail.uia059466.liska.addeditcatalog.units

class GetAllUnits(private val unitsRepository: UnitsRepository ) {
   fun execute() = unitsRepository.units()
}