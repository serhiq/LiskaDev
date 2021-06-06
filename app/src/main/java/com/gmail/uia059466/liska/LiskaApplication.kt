package com.gmail.uia059466.liska

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.gmail.uia059466.liska.domain.CatalogRepository
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.domain.usecase.MessageRepository

class LiskaApplication:Application() {

    val listRepository: ListRepository
        get() = ServiceLocator.provideListRepository(this)

    val catalogRepository: CatalogRepository
        get() = ServiceLocator.provideCatalogRepository(this)

    val messageRepository:MessageRepository
    get() = ServiceLocator.provideMessageRepository(this)


    override fun onCreate() {
        super.onCreate()

        val prefs = UserPreferencesRepositoryImpl.getInstance(this)
        switchToMode(prefs.getCurrentNightMode())

    }
    private fun isPreAndroid10() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    private fun switchToMode(selectedNow: Mode) {
        val mode= when(selectedNow){
            Mode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Mode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Mode.SYSTEM -> if (isPreAndroid10()) {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            } else {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}