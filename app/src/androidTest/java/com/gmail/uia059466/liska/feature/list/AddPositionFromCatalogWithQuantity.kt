package com.gmail.uia059466.liska.feature.list

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.RotoDevice
import com.gmail.uia059466.liska.ServiceLocator
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.data.database.ListEdit
import com.gmail.uia059466.liska.domain.CatalogRepository
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
class AddPositionFromCatalogWithQuantity83() {
    private lateinit var listRepository: ListRepository
    private lateinit var catalogRepository: CatalogRepository
    val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun init() {
        listRepository =
            ServiceLocator.provideListRepository(
                ApplicationProvider.getApplicationContext()
            )

        catalogRepository =
            ServiceLocator.provideCatalogRepository(
                ApplicationProvider.getApplicationContext()
            )

        runBlocking {
            listRepository.deleteAllList()
            catalogRepository.deleteAll()
        }
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun addPositionList83(){
        runBlocking {
            listRepository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(), id = 0
                )
            )
            catalogRepository.insert(CatalogDatabase(
                id = 0,
                title = "first_catalog",
                order = 0,
                list = listOf(CatalogItem("catalog_item",
                    quantity = 2,
                    unit = "",
                    isSelected = false
                )),
                lastModificationDate = 0,
                creationDate = 0
            ))
        }
//      Given
        val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
        val screen= RotoDevice(device).openListsScreen()
        val list= screen.openList(0)
        sleep(300)
        list.checkSizeRecycler(0)


//      When
        list.enableEditMode()
        val selectScreen=list.openCatalogWithQuantity()
        selectScreen.clickOnPosition(0)
        selectScreen.clickOnPlusOnPosition(0)
        device.pressBack()
        device.pressBack()

//      Then
        list.checkItemView(0, "catalog_item, 2", false)
        list.checkSizeRecycler(1)


        device.pressBack()
        screen.openList(0)

        list.checkItemView(0, "catalog_item, 2", false)
        list.checkSizeRecycler(1)

        activityScenario.close()
    }
    @Test
    // Если количество - 1, то добавляется без количества
    fun addPositionList83WhenQuantity1(){
        runBlocking {
            listRepository.insert(
                ListEdit(
                    title = "huimbar", data = mutableListOf(), id = 0
                )
            )
            catalogRepository.insert(CatalogDatabase(
                id = 0,
                title = "first_catalog",
                order = 0,
                list = listOf(CatalogItem("catalog_item",
                    quantity = 1,
                    unit = "",
                    isSelected = false
                )),
                lastModificationDate = 0,
                creationDate = 0
            ))
        }
//      Given
        val activityScenario = ActivityScenario.launch(MainActivityImpl::class.java)
        val screen= RotoDevice(device).openListsScreen()
        val list= screen.openList(0)
        sleep(300)
        list.checkSizeRecycler(0)


//      When
        list.enableEditMode()
        val selectScreen=list.openCatalogWithQuantity()
        selectScreen.clickOnPosition(0)
        selectScreen.clickOnPlusOnPosition(0)
        device.pressBack()
        device.pressBack()

//      Then
        list.checkItemView(0, "catalog_item", false)
        list.checkSizeRecycler(1)


        device.pressBack()
        screen.openList(0)

        list.checkItemView(0, "catalog_item", false)
        list.checkSizeRecycler(1)

        activityScenario.close()
    }

}