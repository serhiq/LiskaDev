package com.gmail.uia059466.liska.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gmail.uia059466.liska.data.ListDisplay

@Entity(tableName = "lists")
data class ListDatabase(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        
        val title: String,
        val order: Int,
        @TypeConverters(DisplayItemTypeConverter::class)
        val list: List<ListItem>,
        val isCatalog: Boolean = false,
        
        val lastModificationDate: Long,
        val creationDate: Long,
        
        ) {
  
  fun createInfo(): String {
    return "${list.count { it.isChecked }} / ${list.size} "
  }
  
  fun tryComplectedTask(index: Int, itemStr: String): Boolean {
    val isPossible = true
    if (isPossible && list[index].title == itemStr) {
      list[index].isChecked = !list[index].isChecked
      return true
    }
    return false
  }
}

fun List<ListDatabase>.toDisplayList(): List<ListDisplay> {
  return this.map {
    ListDisplay(
            id = it.id,
            title = it.title,
            description = it.createInfo(),
            order = it.order,
            dataCreate = it.creationDate,
            dataModification = it.lastModificationDate,
            veryLongDescription = createVeryLongDescription(it.list)
               )
  }
}


fun createVeryLongDescription(items: List<ListItem>): String {
  return when (items.size) {
    0 -> ""
    in 1..10 -> arroundBox(items)
    else -> arroundBox(items.subList(0, 9)) + "\n   …"
  }
  
}

fun arroundBox(items: List<ListItem>): String {
  val sb = StringBuilder()
  var isFirst = true
  val checked = '☑'
  val notChecked = '☐'
  
  for (m in items) {
    if (isFirst) {
      
      sb.append(" ${if (m.isChecked) checked else notChecked} ${m.title}")
      isFirst = false
    } else {
      sb.append("\n")
      sb.append(" ${if (m.isChecked) checked else notChecked} ${m.title}")
    }
  }
  return sb.toString()
}


fun ListDatabase.toListEdit(): ListEdit {
  return ListEdit(title = this.title, data = this.list.toMutableList(), id = this.id)
}