package com.gmail.uia059466.liska

import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.data.database.ListDatabase
import com.gmail.uia059466.liska.data.database.ListEdit

class FakeDataCatalog {
    companion object {
        fun lists(): List<ListEdit> {
            val first= ListEdit(
                id = 0,
                title = "list_1", data = mutableListOf()
            )
            return listOf(first)
        }

        fun catalogs(): List<CatalogDatabase> {
            val first= CatalogDatabase(
                id = 0,
                title = "1_one",
                order = 0,
                list = listOf(CatalogItem(title = "fruis", quantity = 0, unit = "", isSelected = false)),
                lastModificationDate = 0,
                creationDate = 0
            )
            val second= CatalogDatabase(
                id = 0,
                title = "2_two",
                order = 0,
                list = listOf(),
                lastModificationDate = 0,
                creationDate = 0
            )
            val third= CatalogDatabase(
                id = 0,
                title = "3_third",
                order = 0,
                list = listOf(),
                lastModificationDate = 0,
                creationDate = 0
            )
            return listOf(first,second,third)
        }
    }
}