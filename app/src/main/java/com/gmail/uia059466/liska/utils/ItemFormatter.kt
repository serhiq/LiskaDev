package com.gmail.uia059466.liska.utils

class ItemFormatter {
    companion object{
        fun createStringItem(title: String, quantity: Int?, unit: String):String{
            return when{
                quantity!=null&&quantity==0&&unit.isBlank() -> "${title}"
                quantity!=null&&quantity==0&&unit.isNotBlank()-> "${title}, ${unit}"
                quantity!=null&&unit.isBlank() -> "${title}, ${quantity}"
                quantity!=null&&unit.isNotBlank()-> "${title}, ${quantity} ${unit}"
                quantity==null&&unit.isNotBlank()-> "${title}, ${unit}"
                quantity==null&&unit.isBlank()-> "$title"
                else -> throw Exception("no correct")
            }
        }
    }
}