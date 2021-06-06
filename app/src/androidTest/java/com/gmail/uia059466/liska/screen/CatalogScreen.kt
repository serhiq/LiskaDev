package com.gmail.uia059466.liska.screen

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R

class CatalogScreen(on: UiDevice) : BaseScreen(on) {
    /*
  один в один логика что и в listdetailed
   */
    override val contentLayout: Int= R.id.list_detailed_content

    private val save_word_button = R.id.save_word_button
    private val enter_word_edit_text = R.id.enter_word_edit_text

    private val list = R.id.list

    private val menu_edit_mode = R.id.menu_add_mode

    private val menu_delete_list = R.string.menu_delete_catalog


    fun enterText(text: String){
        onView(ViewMatchers.withId(enter_word_edit_text)).perform(click())

    }


    fun enableEditMode(){
        onView(ViewMatchers.withId(menu_edit_mode)).perform(click())
        Thread.sleep(200)
    }

    fun clickOnPlus() {
        onView(ViewMatchers.withId(save_word_button)).perform(click())
    }

    fun swipeUpOnList() {
        onView(ViewMatchers.withId(list)).perform(ViewActions.swipeUp())
    }

    fun clickOnHome() {
        onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
//        onView(withId(menu_home)).perform(click())
    }
    fun clickOnCatalogContent(i: Int){
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


    override fun checkDefaultLayout() {
//        onView(withId(contentLayout)).check(matches(isDisplayed()))
//        onView(withId(emailTv)).check(matches(isDisplayed()))
//        onView(withId(deleteButton)).check(matches(isDisplayed()))
//        onView(withId(signOutButton)).check(matches(isDisplayed()))
    }

    fun clickOnMenuDelete() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())
        onView(ViewMatchers.withText(menu_delete_list)).perform(click())
    }
}