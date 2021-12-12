package com.gmail.uia059466.liska.stuff

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Warehouses")

class Warehouse(
    @PrimaryKey()
    val uuid: String,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "data_created")
    val dataCreated: Long,
    @ColumnInfo(name = "data_update")
    val dataUpdated: Long,
    val order: Int )



