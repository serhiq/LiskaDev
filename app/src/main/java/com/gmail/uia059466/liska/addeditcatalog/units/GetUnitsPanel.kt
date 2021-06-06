package com.gmail.uia059466.liska.addeditcatalog.units

class GetUnitsPanel(
        private val unitsRepository: UnitsRepository
                   ) {
  
  fun execute(): UnitPanelState {
    return UnitPanelState.createNewState(
            selected = unitsRepository.selected(), favs = unitsRepository.favorites())
  
  }
}