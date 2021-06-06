package com.gmail.uia059466.liska.addeditcatalog.units

class UnitPanelState(
        val units:List<String>,
        val currentIndex:Int=0
) {
  companion object{
    fun createNewState(selected:String,favs:List<String>):UnitPanelState{
      val first = favs.getOrNull(0)?:""
      val second= favs.getOrNull(1)?:""
      val third = favs.getOrNull(2)?:""
      val four  = favs.getOrNull(3)?:""
     
      if (selected.isBlank()){
        val units= mutableListOf<String>("",first,second,third,four)
  
        return UnitPanelState(currentIndex = 0,
                              units =units)      }
      
      val needAddingSelectUnit=favs.indexOf(selected)<0
      if (!needAddingSelectUnit){
        val units= listOf<String>("",first,second,third,four)
        val indexSelected=units.indexOf(selected)
  
        return UnitPanelState(currentIndex = indexSelected,
                              units =units)
      }else{
        val units= mutableListOf<String>("",first,second,third,"")
  
        for ((index,lu) in units.withIndex()){
          if (index==0) continue
          if (lu.isBlank()) {
            units[index]=selected
            break
          }
        }
        val indexSelected=units.indexOf(selected)
  
        return UnitPanelState(currentIndex = indexSelected,
                              units =units)
      }
      
      }

  
  }
  
}