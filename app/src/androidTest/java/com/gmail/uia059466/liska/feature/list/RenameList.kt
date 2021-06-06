package com.gmail.uia059466.liska.feature.list

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.RotoDevice
import com.gmail.uia059466.liska.ServiceLocator
import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.data.database.ListItem
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.main.MainActivityImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
@LargeTest
class RenameListTest() {
    private lateinit var repository: ListRepository
    val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun init() {
        repository =
            ServiceLocator.provideListRepository(
                ApplicationProvider.getApplicationContext()
            )
        runBlocking {
            repository.deleteAllList()
        }
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun renameList87(){
        runBlocking {
            repository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(), id = 0
                )
            )
        }
//      Given
        val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
        val screen= RotoDevice(device).openListsScreen()
        val list= screen.openList(0)
        sleep(300)

//      When
        list.enableEditMode()
        val renameDialog=list.clickOnCustomTitle()
        renameDialog.replaceText("newTitle")
        renameDialog.clickOk()


        list.checkCustomTitle("newTitle*")
        device.pressBack()
        list.checkTitle("newTitle")
        device.pressBack()

        screen.checkItemInList(title="newTitle",0)
        screen.openList(0)

//      Then
        list.checkTitle("newTitle")

        activityScenario.close()
    }
}