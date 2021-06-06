package com.gmail.uia059466.liska.feat

import com.gmail.uia059466.liska.BaseTest
import com.gmail.uia059466.liska.RotoDevice
import com.gmail.uia059466.liska.screen.ListDetailScreen
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class HideKeyboardListEdit : BaseTest() {

    @Test
/*    Given  когда фрагмент открыт, клавиатура закрыта
      When   если ввести текст
      Then   клавиатура открыта
        */

    fun basicTest() {
//      Given
        val listDetailed = navigateToListDetailedEdit()

//      When
        listDetailed.clickOnEditText()
        listDetailed.clickOnPlus()

//      Then
        sleep(500)
        assertTrue(listDetailed.isKeyboardOpen())
    }

    private fun navigateToListDetailedEdit(): ListDetailScreen {
        val listScreen = RotoDevice(device).openListsScreen()
        val listDetailed = listScreen.openList(0)
        listDetailed.enableEditMode()
        return listDetailed
    }

    @Test
/*    Given  когда фрагмент открыт, вводиться текст
               When   свайпнуть на контенте
               Then   клавиатура открыта
        */
//    если свайпнуть то клава закроется. при свайпе клава закрываться только если в этом есть смысл
//    если перемещение будеть больше. так что не работает если единиц измерения мало
    fun hideKeyBoardWhenScrolled() {
        val listDetailed = navigateToListDetailedEdit()
        listDetailed.clickOnEditText()
        listDetailed.clickOnPlus()
        sleep(500)
        assertTrue(listDetailed.isKeyboardOpen())
        listDetailed.swipeUpOnList()
        sleep(500)
        assertFalse(listDetailed.isKeyboardOpen())
    }

    /*    Given  когда фрагмент открыт, вводиться текст
               When   нажать на телефоне назад
               Then   клавиатура закрыта
    */

    @Test
    fun hideWhenBackPressed() {
        val listDetailed = navigateToListDetailedEdit()
        listDetailed.clickOnEditText()
        listDetailed.clickOnPlus()
        sleep(500)
        assertTrue(listDetailed.isKeyboardOpen())

//    Then
        device.pressBack()
        sleep(500)
        assertFalse(listDetailed.isKeyboardOpen())

    }

    /*    Given  когда фрагмент открыт, вводиться текст
               When   нажать на панели инструментов назад
               Then   клавиатура закрыта
    */
    @Test
    fun hideWhenBackPressedOnPanel() {
//  Given
        val listDetailed = navigateToListDetailedEdit()
        listDetailed.clickOnEditText()
        listDetailed.clickOnPlus()
        sleep(500)
        assertTrue(listDetailed.isKeyboardOpen())
//  When
        listDetailed.clickOnHome()
        sleep(500)
//   Then
        assertFalse(listDetailed.isKeyboardOpen())

    }

    /*    Given  когда фрагмент открыт, вводиться текст
           When   нажать на выбор номенклатуры
           Then   клавиатура закрыта
*/
    @Test
    fun hideWhenClickOnCatalog() {
//  Given
        val listDetailed = navigateToListDetailedEdit()
        listDetailed.clickOnEditText()
        listDetailed.clickOnPlus()
        sleep(500)
        assertTrue(listDetailed.isKeyboardOpen())

//  When
        listDetailed.clickOnOCatalogOpen()
        sleep(500)
        assertFalse(listDetailed.isKeyboardOpen())

        device.pressBack()
//   Then
        assertFalse(listDetailed.isKeyboardOpen())

    }
}