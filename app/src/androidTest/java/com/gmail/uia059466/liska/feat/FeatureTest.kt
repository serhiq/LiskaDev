package com.gmail.uia059466.liska.feat

import com.gmail.uia059466.liska.BaseTest
import com.gmail.uia059466.liska.RotoDevice
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import org.junit.Test

class FeatureTest : BaseTest() {
  
  
  @Test
  fun canSortList() {
    val d = RotoDevice(device)
//        screenShot("welcome")
    val s = d.openListsScreen().openSortList()
//        screenShot("sort_list")
    s.selectOrder(SortOrder.A_Z)
    d.checkOpen(RotoDevice.type.LIST)
//        screenShot("after_sort")
  }
  
  fun canManualSortList() {
    val d = RotoDevice(device)
//        screenShot("welcome")
    val s = d.openListsScreen().openSortList()
//        screenShot("sort_list")
    s.selectOrder(SortOrder.MANUAL_SORT)
    d.checkOpen(RotoDevice.type.LIST)
//        screenShot("after_sort")
  }
  
  @Test
  fun canOpenCatalogList() {
    val listScreen = RotoDevice(device).openListsScreen()
    val listDetailed = listScreen.openList(0)
    listDetailed.enableEditMode()
    val catalogSelect = listDetailed.openCatalog()
    catalogSelect.whenOpenCatalogDisplayRoot()
    sleep(200)
    catalogSelect.whenOpenCatalogDisplayList(0)
    device.pressBack()
    sleep(200)
    catalogSelect.whenOpenCatalogDisplayRoot()
  }
}