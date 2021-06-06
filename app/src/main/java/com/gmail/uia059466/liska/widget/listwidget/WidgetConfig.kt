package com.gmail.uia059466.liska.widget.listwidget

import android.appwidget.AppWidgetManager
import com.gmail.uia059466.liska.widget.WidgetTheme

class WidgetConfig(
        var idWidget: Int= AppWidgetManager.INVALID_APPWIDGET_ID,
        var idList:Long=0,
        var nameList:String="",
        var displayList:String="",
        var isDisplayIcon:Boolean=false,
        var isListDeleted:Boolean=false,
        var theme:WidgetTheme= WidgetTheme.LIGHT,
        val isShortcurt:Boolean=false)
{
        var errorMessage=""

        fun toStingStr():String{
                val numms:List<String> =
                        mutableListOf(
                                idWidget.toString(),
                                idList.toString(),
                                nameList.toString(),
                                displayList.toString(),
                                isDisplayIcon.toString(),
                                isListDeleted.toString(),
                                theme.rawValue,
                                isShortcurt.toString())

                return numms.joinToString(separator = "!@!")
        }

        fun isCorrect(): Boolean {
                if (idList == 0L) {
                        errorMessage = "Select list"; return false
                }
                errorMessage = ""
                return true

        }

        fun requestCheckbox(complected: Boolean): Int {
                        return when{
            complected -> theme.checkboxComplected
            else ->theme.checkbox
        }
        }

        companion object {
                fun fromString(str: String): WidgetConfig? {
                        try {
                                val array = str.split("!@!")
                                return WidgetConfig(
                                        idWidget =array[0].toInt(),
                                        idList =array[1].toLong(),
                                        nameList = array[2],
                                        displayList = array[3],
                                        isDisplayIcon =  array[4].toBoolean(),
                                        isListDeleted =  array[5].toBoolean(),
                                        theme = WidgetTheme.fromString(array[6]),
                                        isShortcurt = array[7].toBoolean())
                        } catch (e: Exception) {

                        }
                        return null
                }

        }
}