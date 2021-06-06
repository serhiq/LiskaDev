package com.gmail.uia059466.liska.selectfromcatalogquantity

interface CatalogSelectListener {
    fun onCatalogClicked(catalogId: Long, title: String)
    fun onCreateItemClicked(title: String)
}