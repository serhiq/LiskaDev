
package com.gmail.uia059466.liska

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.gmail.uia059466.liska.data.CatalogRepositoryImpl
import com.gmail.uia059466.liska.data.ListRepositoryImpl
import com.gmail.uia059466.liska.data.database.AppDatabase
import com.gmail.uia059466.liska.data.database.SeedDatabaseWorker
import com.gmail.uia059466.liska.data.database.migration.Migration_1_2
import com.gmail.uia059466.liska.domain.CatalogRepository
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.domain.WarehouseRepository
import com.gmail.uia059466.liska.domain.usecase.MessageRepository
import com.gmail.uia059466.liska.domain.usecase.MessageRepositoryImpl

object ServiceLocator {

    private val lock = Any()
    private var database: AppDatabase? = null
    @Volatile
    var listRepository: ListRepository? = null
        @VisibleForTesting set

    @Volatile
    var catalogRepository: CatalogRepository? = null
        @VisibleForTesting set

    @Volatile
    var messageRepository: MessageRepository? = null
        @VisibleForTesting set

    @Volatile
    var warehouseRepository: WarehouseRepository? = null
        @VisibleForTesting set

    fun provideMessageRepository(context: Context): MessageRepository {
        synchronized(this) {
            return messageRepository ?: createMessageRepository(context)
        }
    }
    private fun createMessageRepository(context: Context): MessageRepository {
        val newRepo = MessageRepositoryImpl.getInstance()
        messageRepository = newRepo
        return newRepo
    }

    fun provideListRepository(context: Context): ListRepository {
        synchronized(this) {
            return listRepository ?: createListsRepository(context)
        }
    }

    fun provideWarehouseRepository(context: Context): WarehouseRepository {
        synchronized(this) {
            return warehouseRepository ?: createWarehouseRepository(context)
        }
    }

    private fun createListsRepository(context: Context): ListRepository {
        val newRepo = ListRepositoryImpl(dao=createDataSource(context).listingDao(),configRepository = UserPreferencesRepositoryImpl.getInstance(context))
        listRepository = newRepo
        return newRepo
    }

    private fun createCatalogsRepository(context: Context): CatalogRepository {
        val newRepo = CatalogRepositoryImpl(createDataSource(context).catalogDao())
              catalogRepository = newRepo
        return newRepo
    }

    private fun createWarehouseRepository(context: Context): WarehouseRepository {
        val repo = WarehouseRepositoryImpl(createDataSource(context).warehouseDao())
              warehouseRepository = repo
        return repo
    }

    private fun createDataSource(context: Context): AppDatabase {
       return database ?: createDataBase(context)

    }

    private fun createDataBase(context: Context): AppDatabase {
                synchronized(this) {

                    val result = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "liska.db"
                                                     )
                        .addMigrations(Migration_1_2())
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                val request: OneTimeWorkRequest = OneTimeWorkRequest.Builder(
                                    SeedDatabaseWorker::class.java)
                                    .build()
                                WorkManager.getInstance(context).enqueue(request)
                            }
                        })
                        .build()
                    database = result
                    return result
                }
    }

    fun provideCatalogRepository(context: Context): CatalogRepository {
        synchronized(this) {
            return catalogRepository ?: createCatalogsRepository(context)
        }
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            listRepository = null
        }
    }
}
