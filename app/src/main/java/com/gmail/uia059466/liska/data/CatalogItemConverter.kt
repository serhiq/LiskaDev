package com.gmail.uia059466.liska.data

import com.gmail.uia059466.liska.data.database.CatalogItem

class CatalogItemConverter {
    private val divider = "!$!"
    private val dividerItem = "-+-"

    fun toStringList(item: CatalogItem): String {
        return "${item.title}$divider${item.quantity}$divider${item.unit}"
    }

    fun fromString(itemString: String): CatalogItem? {
        val list = itemString.split(divider)
        return try {
            val title = list[0]
            val quantity=list[1]
            val unit=list[2]
            return CatalogItem(title = title, quantity = quantity.toIntOrNull()?:0, unit = unit, isSelected = false)

        } catch (e: Exception) {
            null
        }
    }

    fun toStringList(list: List<CatalogItem>): String {
        val listsString = list.map { toStringList(it) }
        return listsString.joinToString(dividerItem)
    }

    fun fromStringList(itemString: String): List<CatalogItem> {
        return try {
            val listStringItem = itemString.split(dividerItem)
            val items = listStringItem.map {
                fromString(it)
            }
            return items.filterNotNull()
        } catch (e: Exception) {
            emptyList<CatalogItem>()
        }
    }
}
