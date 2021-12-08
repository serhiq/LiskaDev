package com.gmail.uia059466.liska.addeditcatalog.units


class SaveSelectedUnit(private val unitsRepository: UnitsRepository) {
    fun execute(selected: String) {
        unitsRepository.saveSelected(selected)
    }
}