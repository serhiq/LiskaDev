package com.gmail.uia059466.liska.feat

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import com.gmail.uia059466.liska.BaseTest
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.RotoDevice
import com.gmail.uia059466.liska.screen.SelectCatalogScreen
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class HideKeyboardCatalogSelect : BaseTest() {

    @Test
/*    Given  фрагмент открыт, клавиатура закрыта
      When   если ввести текст в поиск
      Then   клавиатура открыта
        */

    fun basicTest() {
//      Given
        val screen = navigateToSelectCatalog()

//      When
        screen.enterSearch("dd")

//      Then
        sleep(500)
        assertTrue(screen.isKeyboardOpen())
    }

    private fun navigateToSelectCatalog(): SelectCatalogScreen {
        val s = RotoDevice(device)
            .openListsScreen()
            .openList(0)

        s.enableEditMode()

        return s.clickOnOCatalogOpen()
    }

    @Test
/*    Given  фрагмент открыт, вводиться текст, открывается клавиатура
               When   свайпнуть на контенте
               Then   клавиатура закрыта
        */
//    если свайпнуть то клава закроется. при свайпе клава закрываться только если в этом есть смысл
//    если перемещение будеть больше. так что не работает если единиц измерения мало
    fun hideKeyBoardWhenScrolled() {
        val screen = navigateToSelectCatalog()
        screen.enterSearch("dd")
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
        //  Given
        val screen = navigateToSelectCatalog()
        screen.enterSearch("dd")
        sleep(500)
        assertTrue(screen.isKeyboardOpen())
        //  When
        device.pressBack()
        sleep(500)

        //  Then
        assertFalse(screen.isKeyboardOpen())
    }

    /*    Given  когда фрагмент открыт, вводиться текст
               When   нажать на панели инструментов назад
               Then   клавиатура закрыта
    */
    @Test
    fun hideWhenBackPressedOnPanel() {
//  Given
        val screen = navigateToSelectCatalog()
        screen.enterSearch("dd")
        sleep(500)
        assertTrue(screen.isKeyboardOpen())
//  When
        screen.clickOnHome()
        sleep(500)
//   Then
        assertFalse(screen.isKeyboardOpen())
    }

    /*    Given  когда фрагмент(root folder) открыт, вводиться текст
           When   нажать на просмотр каталога
           Then   клавиатура закрыта
*/
//    0-  создание позиции
//     1 - первое
    @Test
    fun hideWhenClickOnCatalog() {
//  Given
        val screen = navigateToSelectCatalog()
        Espresso.onView(ViewMatchers.withId(R.id.search_et)).perform(click())
        sleep(500)
        assertTrue(screen.isKeyboardOpen())

//  When
        screen.clickOnContent(0)
        sleep(500)
//   Then
        assertFalse(screen.isKeyboardOpen())

    }


    /*    Given  когда фрагмент(root folder) открыт, вводиться текст
           When   нажать на добавить в reycler view
           Then   клавиатура закрыта, или открыта?
*/


    /*    Given  когда фрагмент(root folder) открыт, вводиться текст
           When   нажать на создать в reycler view
           Then   клавиатура открыта
    */


    /*    Given  когда фрагмент(root folder) открыт, вводиться текст
           When   нажать на крестик
           Then   клавиатура закрыта,запрос очищен
*/

    @Test
    fun hideWhenClearSearchInRoot() {
//  Given
        val screen = navigateToSelectCatalog()
        screen.enterSearch("dd")
        sleep(500)

//  When
        screen.clickOnClear()
        sleep(500)
//   Then
        assertFalse(screen.isKeyboardOpen())

    }
}
