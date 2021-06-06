package com.gmail.uia059466.liska.data.database

data class CatalogItem
        (
        var title: String,
        var quantity:Int,
        val unit:  String="",
        var isSelected: Boolean = false
){
        constructor(title: String) : this(title,0)
}


