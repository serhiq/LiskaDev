package com.gmail.uia059466.liska.selectfromcatalogquantity

import com.gmail.uia059466.liska.catalog.AddItemsInCatalog
import com.gmail.uia059466.liska.data.database.CatalogDatabase

interface CatalogSelect {
    val currentId: Long
    fun search(query: String)
    fun fillAdapter(rawList: List<CatalogDatabase>)
    fun setupRootFolder()
    fun setupViewCurrentFolder()
    fun getCatalogCommand(): List<AddItemsInCatalog>
    fun getSelected(): List<String>
    fun addItem(title: String, id: Long)
    fun addItem(title: String)
}