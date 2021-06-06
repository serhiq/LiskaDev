package com.gmail.uia059466.liska.feature.list

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
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
class CheckboxStateSavedTest {

    private lateinit var repository: ListRepository

    @Before
    fun init() {
        repository =
            ServiceLocator.provideListRepository(
                getApplicationContext()
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
    fun setCheckBoxAndBack() {
        runBlocking {
            repository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(
                        ListItem("ONE", isChecked = false),
                        ListItem("TWO", isChecked = false)
                    ), id = 0
                )
            )
            val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
//      Given

            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val listsScreen = RotoDevice(device).openListsScreen()
            val listDetailScreen = listsScreen.openList(0)

//      When
            listDetailScreen.checkItemView(0, "ONE", false)
            listDetailScreen.checkItemView(1, "TWO", false)

            listDetailScreen.clickOnList(0)
            listDetailScreen.checkItemView(0, "ONE", true)

            device.pressBack()
            listsScreen.openList(0)

//      Then
            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityScenario.close()
        }
    }

    @Test
    fun setCheckBoxAndOpenNavigationDrawerToList() {
        runBlocking {
            repository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(
                        ListItem("ONE", isChecked = false),
                        ListItem("TWO", isChecked = false)
                    ), id = 0
                )
            )
            val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
//      Given

            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val listsScreen = RotoDevice(device).openListsScreen()
            val listDetailScreen = listsScreen.openList(0)

//      When
            listDetailScreen.checkItemView(0, "ONE", false)
            listDetailScreen.checkItemView(1, "TWO", false)

            listDetailScreen.clickOnList(0)
            listDetailScreen.checkItemView(0, "ONE", true)

            val nv=listDetailScreen.openNavigationDrawer()
            sleep(500)
            nv.openLists()
            listsScreen.openList(0)

//      Then
            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityScenario.close()
        }
    }


    @Test
    fun setCheckBoxToEditModeAndBack() {
        runBlocking {
            repository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(
                        ListItem("ONE", isChecked = false),
                        ListItem("TWO", isChecked = false)
                    ), id = 0
                )
            )
            val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)

//      Given
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val listsScreen = RotoDevice(device).openListsScreen()
            val listDetailScreen = listsScreen.openList(0)

//      When
            listDetailScreen.checkItemView(0, "ONE", false)
            listDetailScreen.checkItemView(1, "TWO", false)

            listDetailScreen.clickOnList(0)
            listDetailScreen.checkItemView(0, "ONE", true)

            listDetailScreen.enableEditMode()
            device.pressBack()

//      Then
            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityScenario.close()
        }
    }

    @Test
    fun setCheckBoxAndRecreateActivity() {
        runBlocking {
            repository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(
                        ListItem("ONE", isChecked = false),
                        ListItem("TWO", isChecked = false)
                    ), id = 0
                )
            )
            val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
//      Given

            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val listsScreen = RotoDevice(device).openListsScreen()
            val listDetailScreen = listsScreen.openList(0)

//      When
            listDetailScreen.checkItemView(0, "ONE", false)
            listDetailScreen.checkItemView(1, "TWO", false)

            listDetailScreen.clickOnList(0)
            listDetailScreen.checkItemView(0, "ONE", true)

            activityScenario.recreate()
            sleep(900)

//      Then
            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityScenario.close()
        }
    }

    @Test
    fun setCheckBoxAndMoveToState() {
        runBlocking {
            repository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(
                        ListItem("ONE", isChecked = false),
                        ListItem("TWO", isChecked = false)
                    ), id = 0
                )
            )
            val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
//      Given

            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val listsScreen = RotoDevice(device).openListsScreen()
            val listDetailScreen = listsScreen.openList(0)

//      When
            listDetailScreen.checkItemView(0, "ONE", false)
            listDetailScreen.checkItemView(1, "TWO", false)

            listDetailScreen.clickOnList(0)
            listDetailScreen.checkItemView(0, "ONE", true)

//            activityScenario.moveToState(Lifecycle.State.CREATED)
//            activityScenario.moveToState(Lifecycle.State.RESUMED)

//          сделать еще один тест
            activityScenario.moveToState(Lifecycle.State.STARTED)
            activityScenario.moveToState(Lifecycle.State.RESUMED)

            sleep(900)
//      Then
            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityScenario.close()
        }
    }
}