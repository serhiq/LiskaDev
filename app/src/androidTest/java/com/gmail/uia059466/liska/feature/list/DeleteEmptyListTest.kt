package com.gmail.uia059466.liska.feature.list

import com.gmail.uia059466.liska.BaseTest
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.RotoDevice
import org.junit.Test

class DeleteEmptyListTest: BaseTest() {
    @Test
    fun deleteEmptyList_from_fab_29(){
//      Given
        val screen= RotoDevice(device).openListsScreen()
        screen.createListFromFab()
        sleep(300)

//      When
       device.pressBack()
       device.pressBack()

//      Then
        screen.isShow()
        screen.isDisplayedSnackbar(R.string.delete_empty_list)
   }

    @Test
    fun deleteEmptyList_from_navigation_drawer_29(){
//      Given
        val screen= RotoDevice(device).openListsScreen()
        val drawer=screen.openNavigationDrawer()
        drawer.createList()

//      When
        device.pressBack()
        device.pressBack()

//      Then
        screen.isShow()
        screen.isDisplayedSnackbar(R.string.delete_empty_list)
    }
}