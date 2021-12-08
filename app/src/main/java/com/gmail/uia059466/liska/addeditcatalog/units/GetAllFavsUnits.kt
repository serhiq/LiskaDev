package com.gmail.uia059466.liska.addeditcatalog.units

class GetAllFavsUnits(private val unitsRepository: UnitsRepository) {
   fun execute() = unitsRepository.favorites()
}