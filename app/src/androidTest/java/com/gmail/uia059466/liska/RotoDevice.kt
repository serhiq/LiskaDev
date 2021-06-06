package com.gmail.uia059466.liska

import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.screen.*
import junit.framework.Assert.assertTrue

class RotoDevice(val device: UiDevice) {
  
  fun openListsScreen(): ListsScreen {
    return ListsScreen(on = device)
  }
  
  fun checkOpen(type: type) {
    val isDisplayed=when(type){
      RotoDevice.type.LIST ->ListsScreen(on = device).isShow()
      RotoDevice.type.DIALOG_ORDER ->SelectSortDialogScreen(on = device).isShow()
      RotoDevice.type.MANUAL_SORT -> TODO()
    }
    assertTrue(isDisplayed)
  }



    fun openFeedback(): FeedBackScreen {
      val listScreen = RotoDevice(device).openListsScreen()
      return listScreen
                       .openNavigationDrawer()
                       .openSetting()
                       .openFeedBack()
    }

  fun openSetting(): SettingScreen {
    val listScreen = RotoDevice(device).openListsScreen()
    return listScreen
      .openNavigationDrawer()
      .openSetting()
  }


  fun openCatalog(): CatalogScreen {
    val listScreen = RotoDevice(device).openListsScreen()
    return listScreen
      .openNavigationDrawer()
      .openCatalog()
  }


    enum class type{
LIST,DIALOG_ORDER,MANUAL_SORT
  }
}