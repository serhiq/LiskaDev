package com.gmail.uia059466.liska.feature.list

import android.content.pm.ActivityInfo
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
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
import org.junit.Rule
import org.junit.Test

class ScreenRotationTest {
//    CR-5 test procedure
    private lateinit var repository: ListRepository

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
    @get:Rule
    var activityRule = ActivityTestRule(MainActivityImpl::class.java)

    @Test
    fun setCheckBoxAndRotateToLand() {
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
            listDetailScreen.checkItemView(0, "ONE", false)
            listDetailScreen.checkItemView(1, "TWO", false)

            listDetailScreen.clickOnList(0)
            listDetailScreen.checkItemView(0, "ONE", true)

//      When
            activityRule.activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            Thread.sleep(1000)

//      Then
            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityScenario.close()
        }
    }

    @Test
    fun setCheckBoxChangeOrientationThreeTimes() {
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
            listDetailScreen.checkItemView(0, "ONE", false)
            listDetailScreen.checkItemView(1, "TWO", false)

            listDetailScreen.clickOnList(0)
            listDetailScreen.checkItemView(0, "ONE", true)

//      When
            activityRule.activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Thread.sleep(1000)

            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityRule.activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Thread.sleep(1000)

            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityRule.activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Thread.sleep(1000)

//      Then
            listDetailScreen.checkItemView(0, "ONE", true)
            listDetailScreen.checkItemView(1, "TWO", false)

            activityScenario.close()
        }
    }
}