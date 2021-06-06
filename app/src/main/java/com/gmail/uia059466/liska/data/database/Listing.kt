package com.gmail.uia059466.liska.data.database


data class ListEdit(
    var title: String,
    val data: MutableList<ListItem>,
    val id: Long
) {
    fun createInfo(): String {
        return "${data.getNumComplected()} / ${data.size} "
    }

    fun isComplected(): Boolean {

        return if (data.isEmpty()) false
        else {
            val numCompleted = data.getNumComplected()
            numCompleted == data.size
        }
    }
}


fun List<ListItem>.getSelectedCount():Int{
    var count = 0
    count = this.count { it.isSelected }
    return count
}
data class ListItem
    (
    var title: String,
    var isChecked: Boolean = false,
    var isSelected: Boolean = false
)

fun List<ListItem>.getNumComplected(): Int {
    val checked = this.filter { it.isChecked }
    return checked.size
}

fun List<ListItem>.clearSelected(): List<ListItem> {
    return this.map {
        ListItem(
            title = it.title,
            isChecked = it.isChecked,
            isSelected = it.isSelected
        )
    }
}

class DisplayItemConverter {
    private val divider = "!$!"
    private val dividerItem = "-+-"

    fun toStringList(item: ListItem): String {
        val isCheckedString = when (item.isChecked) {
            true -> "1"
            false -> "2"
        }
        return "${item.title}$divider$isCheckedString"
    }

    fun fromString(itemString: String): ListItem? {
        val list = itemString.split(divider)
        return try {
            val title = list[0]
            val isChecked = when (list[1]) {
                "1" -> true
                else -> false
            }
            return ListItem(title = title, isChecked = isChecked)

        } catch (e: Exception) {
            null
        }
    }

    fun toStringList(list: List<ListItem>): String {
        val listsString = list.map { toStringList(it) }
        return listsString.joinToString(dividerItem)
    }

    fun fromStringList(itemString: String): List<ListItem> {
        return try {
            val listStringItem = itemString.split(dividerItem)
            val items = listStringItem.map {
                fromString(it)
            }
            return items.filterNotNull()
        } catch (e: Exception) {
            emptyList<ListItem>()
        }
    }
}

fun listIsEquals(
    new: List<ListItem>,
    old: List<ListItem>
): Boolean {
    if (new.size != old.size) return false
    val areNotEqual = new.asSequence()
        .zip(old.asSequence())
        // check this and other contains same elements at position
        .map { (fromThis, fromOther) -> (fromThis.title == fromOther.title) && (fromThis.isChecked == fromOther.isChecked) }
        // searching for first negative answer
        .contains(false)
    if (areNotEqual) return false

    return true
}
