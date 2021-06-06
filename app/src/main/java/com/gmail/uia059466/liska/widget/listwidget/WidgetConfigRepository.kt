package com.gmail.uia059466.liska.widget.listwidget

interface WidgetConfigRepository {
    fun getById(id: Int): WidgetConfig?
    fun save(config: WidgetConfig)
    fun update(config: WidgetConfig)
    fun deleteById(id: Int)
    fun setupDeletedListForConfig(idList: Long)
    fun getBigWidget(): IntArray
    fun getSmallWidget(): IntArray
}
