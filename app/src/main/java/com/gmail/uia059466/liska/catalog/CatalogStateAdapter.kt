package com.gmail.uia059466.liska.catalog

data class CatalogStateAdapter(
    val mode:AdapterViewCatalogMode,
    val currentId:Long =0L,
    val displayList:MutableList<DisplayCatalog> = mutableListOf<DisplayCatalog>()
)