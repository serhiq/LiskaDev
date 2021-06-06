package com.gmail.uia059466.liska.domain

import com.gmail.uia059466.liska.lists.sortorder.SortOrder

interface UserRepository {
    fun readSortOrderList():SortOrder
    fun saveSortOrderList(newSortOrder: SortOrder)

    fun readSortOrderCatalog():SortOrder
    fun saveSortOrderCatalog(newSortOrder: SortOrder)

    fun readIsDisplayFolder():Boolean
    fun saveIsDisplayFolder(isDisplayFolder: Boolean)
}