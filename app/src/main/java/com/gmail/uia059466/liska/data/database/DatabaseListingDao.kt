package com.gmail.uia059466.liska.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseListingDao {
  
  @Query("SELECT * FROM lists")
  suspend fun getAllList(): List<ListDatabase>
  
  @Query("SELECT * FROM lists where isCatalog")
  suspend fun getCatalogList(): List<ListDatabase>
  
  @Query("SELECT * FROM lists where not isCatalog")
  suspend fun getUserList(): List<ListDatabase>
  
  @Query("SELECT * FROM lists where not isCatalog")
  fun observeUserList(): Flow<List<ListDatabase>>
  
  @Query("SELECT * FROM lists WHERE id = :id")
  suspend fun getListById(id: Long): ListDatabase?
 
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: ListDatabase): Long
  
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: List<ListDatabase>)
  
  @Update
  suspend fun update(item: ListDatabase): Int
  
  @Query("DELETE FROM lists WHERE id = :id")
  suspend fun deleteById(id: Long): Int
  
  @Query("DELETE FROM lists")
  suspend fun deleteAll()
  
  @Query("UPDATE lists SET title=:title WHERE id = :id")
  suspend fun updateTitleList(id: Long, title:String)
  
  @Query("UPDATE lists SET list=:items WHERE id = :id")
  suspend fun updateItems(id: Long, items:List<ListItem>)

  @Query("UPDATE lists SET 'order'=:order WHERE id = :id")
  suspend fun updateOrder(id: Long, order:Int)


  @Query("UPDATE lists SET list=:items,lastModificationDate=:datamodification WHERE id = :id")
  suspend fun updateItemsWithDateModifications(datamodification:Long,id: Long, items:List<ListItem>)
  
  @Query("UPDATE lists SET  title=:title,lastModificationDate=:datamodification WHERE id = :id")
  suspend fun updateTitleDateModifications(datamodification:Long,id: Long,title:String)
}