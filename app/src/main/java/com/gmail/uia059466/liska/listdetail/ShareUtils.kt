package com.gmail.uia059466.liska.listdetail

import com.gmail.uia059466.liska.data.database.ListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun createMessage(list: List<ListItem>): String {
  val sb = StringBuilder()
  for (item in list) {
    sb.append("- ${item.title}\n")
  }
  return sb.toString()
}

fun createCurrentDataTitle(): String {
  val currentDate: String =
          SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  return "($currentDate)"
}