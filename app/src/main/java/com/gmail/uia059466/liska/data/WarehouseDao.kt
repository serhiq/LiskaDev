package com.gmail.uia059466.liska.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gmail.uia059466.liska.stuff.Warehouse

@Dao
interface WarehouseDao {
  
  @Query("SELECT * FROM Warehouses")
  suspend fun getAll(): List<Warehouse>
  
  @Query("SELECT * FROM Warehouses WHERE uuid = :uuid")
  suspend fun getByUuid(uuid: String): Warehouse?
 
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: Warehouse): Long
  
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: List<Warehouse>)
  
  @Update
  suspend fun update(item: Warehouse): Int
  
  @Query("DELETE FROM Warehouses WHERE uuid = :uuid")
  suspend fun deleteByUuid(uuid: String): Int
  
  @Query("DELETE FROM Warehouses")
  suspend fun deleteAll()
  
  @Query("UPDATE Warehouses SET title=:title WHERE uuid = :uuid")
  suspend fun updateTitle(uuid: String, title:String)

  @Query("UPDATE Warehouses SET 'order'=:order WHERE uuid=:uuid")
  suspend fun updateOrder(uuid: String, order: Int)

}