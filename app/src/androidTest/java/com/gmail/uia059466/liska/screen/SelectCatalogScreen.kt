package com.gmail.uia059466.liska.screen

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.TestUtils
import com.gmail.uia059466.liska.lists.ListsAdapter
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf
import java.lang.Thread.sleep

/*
**отображаение при запуске после добавления**
    * стрелка назад
    * очистить
    * поле поиска с названием search
    *  каталоги 1_one, 2_one, 3_third
    *

* режим вход в папку
        - appbar с названием папки
        * кнопка назад
        * с не показыаны поле поиска, и очистить

       * fruis - title and check box
       *

     * если нажать на chexbox - от отметиться
     * если нажать на itemview - он отметиться

     *  если нажать добавить откроется поле добавить юнит

     * если нажать назад  появиться режим все папок
     *
* режим поиска

        если набрать fruits в поле поиска  - отоброзиться фрутися
        если нажать onback - стереться и появяться папки
        если нажать клеак все стереться

        *
        * если поле поиска пустое и нажать назад то отправимся назад в список
        *

 */
class SelectCatalogScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.catalog_content

    override fun checkDefaultLayout() {
//        todo
    }
    private val captionRecyclerView = R.id.text



    //    add section
   private val divider = R.id.divider
   private val save_word_button = R.id.save_word_button
   private val enter_word_edit_text = R.id.enter_word_edit_text


   private val list = R.id.list

    //    recycler item   -- folder
    private val catalog_img = R.id.catalog_img
    private val name_tv = R.id.name_tv

    //    recycler item   -- item
    private val title_tv = R.id.title_tv
    private val checkbox = R.id.checkbox

    private val add_fl = R.id.add_fl

//    menu
    private val menu_clear_search = R.id.menu_clear_search

//    Edit text in app bar
    private val search_et = R.id.search_et



    fun openCatalog(){
//
//        onView(withId(menu_edit_mode)).perform(click())
//        sleep(200)
//        onView(withId(menu_add_from_catalog)).perform(click())
    }

    /*
            когда открывается каталог
                стрелка назад
                поле поиска
                список из папок
                и кнопка очистить

     */

    /*
                если ввести буквы, появиться список найденого

     */

//
//    **отображаение при запуске после добавления**
//    * стрелка назад
//    * очистить
//    * поле поиска с названием search
//    *  каталоги 1_one, 2_one, 3_third
//    *
//

    fun whenOpenCatalogDisplayRoot(){
//        onView(withId(R.id.text_action_bar_result))
//            .check(matches(withText("Save")))
        onView(withId(menu_clear_search)).check(matches(isDisplayed()));
        onView(withId(search_et)).check(matches(isDisplayed()));
        onView(allOf(withId(name_tv), withText("1_one"))).check(matches(isDisplayed()));
    }

//
//    * режим вход в папку
//    - appbar с названием папки
//    * кнопка назад
//    * с не показыаны поле поиска, и очистить
//
//    * fruis - title and check box
//    *
//
//    * если нажать на chexbox - от отметиться
//    * если нажать на itemview - он отметиться
//
//    *  если нажать добавить откроется поле добавить юнит
//
//    * если нажать назад  появиться режим все папок
//    *
//
//

    fun whenOpenCatalogDisplayList(i: Int){

        val recyclerView = onView(
           withId(list)
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                i,
                click()
            )
        )
        Thread.sleep(500)
        onView(withId(search_et)).check(matches(not(isDisplayed())))

        val textView2 = onView(
            Matchers.allOf(
                withParent(
                    Matchers.allOf(
                        withId(R.id.toolbar),
                        withParent(IsInstanceOf.instanceOf(LinearLayout::class.java)),
                        withText("1_one")
                    )
                ),
                isDisplayed()
            )
        )
    }

    fun enterSearch(query: String) {
//        onView(withId(search_et)).perform(typeText(query), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(search_et)).perform(typeText(query))
    }

    fun clickOnHome() {
        onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }

    fun clickOnContent(i: Int){
        val recyclerView = onView(

            ViewMatchers.withId(R.id.list)

        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                i,
                click()
            )
        )
        Thread.sleep(200)

    }

    fun clickOnClear() {
        onView(ViewMatchers.withId(menu_clear_search)).perform(click())
    }

    fun swipeUpOnList() {
        onView(ViewMatchers.withId(list)).perform(ViewActions.swipeUp())
    }

    fun clickOnPosition(i: Int) {
        val recyclerView = onView(withId(R.id.list))
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                i,
                click()
            )
        )
    }

    fun clickOnPlusOnPosition(position: Int) {
        onView(TestUtils.withRecyclerView(R.id.list).atPositionOnView(position, R.id.add_img)).perform(
            click())
    }

    companion object{
        fun createAndWait(device:UiDevice):SelectCatalogScreen{
            val screen=SelectCatalogScreen(device)
            screen.waitForScreenToBeDisplayed()
            return screen
        }
    }
}