package com.gmail.uia059466.liska.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CatalogDao {
  
  @Query("SELECT * FROM catalog")
  suspend fun getAll(): List<CatalogDatabase>
  
  @Query("SELECT * FROM catalog WHERE id = :id")
  suspend fun getById(id: Long): CatalogDatabase?
 
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: CatalogDatabase): Long
  
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: List<CatalogDatabase>)
  
  @Update
  suspend fun update(item: CatalogDatabase): Int
  
  @Query("DELETE FROM catalog WHERE id = :id")
  suspend fun deleteById(id: Long): Int
  
  @Query("DELETE FROM catalog")
  suspend fun deleteAll()
  
  @Query("UPDATE catalog SET title=:title WHERE id = :id")
  suspend fun updateTitle(id: Long, title:String)
  
  @Query("UPDATE catalog SET list=:items WHERE id = :id")
  suspend fun updateItems(id: Long, items:List<CatalogItem>)

  @Query("UPDATE catalog SET 'order'=:order WHERE id = :id")
  suspend fun updateOrder(id: Long, order:Int)


  @Query("UPDATE catalog SET list=:items,lastModificationDate=:datamodification WHERE id = :id")
  suspend fun updateItemsWithDateModifications(datamodification:Long,id: Long, items:List<ListItem>)
  
  @Query("UPDATE catalog SET  title=:title,lastModificationDate=:datamodification WHERE id = :id")
  suspend fun updateTitleDateModifications(datamodification:Long,id: Long,title:String)

  @Query("SELECT `order`from catalog ORDER BY `order` DESC LIMIT 1 ")
  suspend fun lastOrder(): Int?

}