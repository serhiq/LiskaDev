package com.gmail.uia059466.liska.data.database

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.database.AppDatabaseInitializer.Companion.requestEnglishData
import com.gmail.uia059466.liska.data.database.AppDatabaseInitializer.Companion.requestRussianData
import kotlinx.coroutines.coroutineScope

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
                        ) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {

            val isRussian = applicationContext.resources.getBoolean(R.bool.isRussian)
            val data = if (isRussian) {
                requestRussianData()
            } else {
                requestEnglishData()
            }
            (applicationContext as LiskaApplication).catalogRepository.insertAll(data)
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
    }
}