package com.gmail.uia059466.liska.screen

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.TestUtils
import com.gmail.uia059466.liska.TestUtils.withRecyclerView
import java.lang.Thread.sleep

class ListDetailScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.list_detailed_content

    override fun checkDefaultLayout() {
//        todo
    }

    //    add section
   private val divider = R.id.divider
   private val save_word_button = R.id.save_word_button
   private val enter_word_edit_text = R.id.enter_word_edit_text

   val list = R.id.list

    //    recycler item
    private val radio = R.id.radio

    val checkbox = R.id.checkbox
    val name_tv = R.id.name_tv
    private val delete_img = R.id.delete_img

    private val handle_img = R.id.handle_img

//    menu
    private val menu_edit_mode = R.id.menu_add_mode
    private val menu_delete_list = R.id.menu_delete_list
    private val menu_send_list = R.id.menu_send_list
    private val menu_copy_list = R.id.menu_copy_list
    private val menu_home = android.R.id.home

    private val menu_add_from_catalog = R.id.menu_add_from_catalog
    private val menu_add_from_catalog_with_quantity = R.id.menu_add_with_quantity


    fun openCatalog(): SelectCatalogScreen {
        onView(withId(menu_add_from_catalog)).perform(click())
        return SelectCatalogScreen(device)
    }

    fun openCatalogWithQuantity(): SelectCatalogScreen {
        onView(withId(menu_add_from_catalog_with_quantity)).perform(click())
        return SelectCatalogScreen(device)
    }



    fun clickOnOCatalogOpen(): SelectCatalogScreen {
        onView(withId(menu_add_from_catalog)).perform(click())
        return SelectCatalogScreen.createAndWait(device)
    }

    fun enableEditMode(){
        onView(withId(menu_edit_mode)).perform(click())
        sleep(200)
    }

    fun clickOnEditText() {
        onView(withId(enter_word_edit_text)).perform(click())

    }
    fun enterTextt(text: String){
        onView(withId(enter_word_edit_text)).perform(typeText(text))


    }
    fun clickOnPlus() {
        onView(withId(save_word_button)).perform(click())
    }

    fun swipeUpOnList() {
        onView(withId(list)).perform(ViewActions.swipeUp())
    }

    fun clickOnHome() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    }

     fun checkItemView(position: Int, title: String, isChecked: Boolean) {
        onView(TestUtils.withRecyclerView(R.id.list).atPositionOnView(position, R.id.name_tv)).check(
            ViewAssertions.matches(ViewMatchers.withText(title))
        )
        val matcher= if (isChecked) ViewMatchers.isChecked() else ViewMatchers.isNotChecked()

        onView(TestUtils.withRecyclerView(R.id.list).atPositionOnView(position, R.id.checkbox)).check(
            ViewAssertions.matches(matcher)
        )   
    }

    fun clickOnList(position: Int) {
        onView(withRecyclerView(list).atPosition(position)).perform(click());    }

    fun openNavigationDrawer():NavigationDrawerScreen{
        onView(ViewMatchers.withContentDescription("open"))
            .perform(click())
        val screen=NavigationDrawerScreen(device)
        screen.waitForScreenToBeDisplayed()
        return screen
    }

    fun pressAdd() {
        onView(withId(R.id.save_word_button)).perform(click());
    }

    fun pressOnDelete(position: Int) {
        onView(TestUtils.withRecyclerView(R.id.list).atPositionOnView(position, R.id.delete_img)).perform(
            click())
    }

    fun checkSizeRecycler(size:Int)  {
        onView(withId(R.id.list)).check(matches(hasChildCount(size)))
    }

    fun clickOnCustomTitle(): RenameListScreen {
        onView(withId(R.id.title_app_bar)).perform(click())
        return RenameListScreen.createAndWait(device)

    }

    fun checkTitle(title:String){
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(title))))
    }
    fun checkCustomTitle(title:String){
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(title))))
    }

    fun sendList() {
        val text = R.string.menu_send_list_text
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(text)).perform(click())
    }

}