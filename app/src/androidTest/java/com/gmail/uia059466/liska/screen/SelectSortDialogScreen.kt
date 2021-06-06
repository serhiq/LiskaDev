package com.gmail.uia059466.liska.screen

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import junit.framework.Assert.assertTrue

class SelectSortDialogScreen(on: UiDevice) : BaseScreen(on) {
  
  override val contentLayout: Int = R.id.select_dialog_listview
 
  private val title= R.string.dialog_select_order_title
  private val listView = on.findObject(UiSelector().resourceId("$id/select_dialog_listview"))
  
  private val numberOfSort: Int
    get() {
      return listView.childCount
    }
  
  fun selectOrder(order: SortOrder) {
    val orders = SortOrder.values()
    val indexTheme = orders.indexOf(order)
    val itemTheme = listView.getChild(UiSelector().index(indexTheme))
    itemTheme.click()
  }
  
  override fun checkDefaultLayout() {
    checkTextDisplayed(title)
    assertTrue(numberOfSort==SortOrder.values().size)
  }
}
