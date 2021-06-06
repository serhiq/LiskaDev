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

@RunWith(AndroidJUnit4::class)
@LargeTest
class DeletePosition83 {
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
    fun deletePositionList82(){
        runBlocking {
            repository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(ListItem("One",isChecked = false)), id = 0
                )
            )
        }
        val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
//      Given
        val screen= RotoDevice(device).openListsScreen()
        val list= screen.openList(0)
        list.checkSizeRecycler(1)
        Thread.sleep(300)

//      When
        list.enableEditMode()
        list.pressOnDelete(0)

//      Then
        list.checkSizeRecycler(0)

        activityScenario.close()
    }

}