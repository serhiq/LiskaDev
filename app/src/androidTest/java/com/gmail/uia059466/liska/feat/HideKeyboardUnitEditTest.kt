package com.gmail.uia059466.liska.feat

import com.gmail.uia059466.liska.BaseTest
import com.gmail.uia059466.liska.RotoDevice
import com.gmail.uia059466.liska.screen.FavoriteUnitsScreen
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class HideKeyboardUnitEditTest : BaseTest() {
    /*    Given  когда фрагмент открыт, клавиатура закрыта
           When   если ввести текст
           Then   клавиатура открыта
    */

      @Test
    fun basicTest() {
//      Given
        val screen = navigateToUnitFavs()

//      When
        screen.enterText("dd")
        screen.clickOnPlus()

//      Then
        sleep(500)
        assertTrue(screen.isKeyboardOpen())
    }

    @Test
/*    Given  когда фрагмент открыт, вводиться текст
               When   свайпнуть на контенте
               Then   клавиатура открыта
        */
//    если свайпнуть то клава закроется. при свайпе клава закрываться только если в этом есть смысл
//    если перемещение будеть больше. так что не работает если единиц измерения мало
    fun hideKeyBoardWhenScrolled() {
        val screen = navigateToUnitFavs()
        screen.enterText("dd")
        screen.clickOnPlus()
        sleep(500)
        assertTrue(screen.isKeyboardOpen())
        screen.swipeUpOnList()
        sleep(500)
        assertFalse(screen.isKeyboardOpen())
    }

    /*    Given  когда фрагмент открыт, вводиться текст
               When   нажать на телефоне назад
               Then   клавиатура закрыта
    */
    @Test
    fun hideWhenBackPressed() {
        val screen = navigateToUnitFavs()
        screen.enterText("dd")
        screen.clickOnPlus()
        sleep(500)
        assertTrue(screen.isKeyboardOpen())

//    Then
        device.pressBack()
        sleep(500)
        assertFalse(screen.isKeyboardOpen())
    }

    /*    Given  когда фрагмент открыт, вводиться текст
           When   нажать на панели инструментов назад
           Then   клавиатура закрыта
*/
    @Test
    fun hideWhenBackPressedOnPanel() {
//  Given
        val screen = navigateToUnitFavs()
        screen.enterText("dd")
        screen.clickOnPlus()
        sleep(500)
        assertTrue(screen.isKeyboardOpen())

//  When
        screen.clickOnHome()
        sleep(500)
//   Then
        assertFalse(screen.isKeyboardOpen())

    }
    private fun navigateToUnitFavs(): FavoriteUnitsScreen {
        val settingScreen = RotoDevice(device).openSetting()
        val screen = settingScreen.openFavUnits()
        screen.enableEditMode()
        return screen
    }
}