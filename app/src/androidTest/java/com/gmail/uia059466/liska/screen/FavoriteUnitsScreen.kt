package com.gmail.uia059466.liska.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R

class FavoriteUnitsScreen(on: UiDevice) : BaseScreen(on) {
    /*
    один в один логика что и в listdetailed
     */
    override val contentLayout: Int= R.id.list_detailed_content

    private val save_word_button = R.id.save_image_btn
    private val enter_word_edit_text = R.id.edit_text

    private val list = R.id.recycler_view

    private val menu_edit_mode = R.id.menu_add_mode
    private val menu_home = android.R.id.home


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
        onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
//        onView(withId(menu_home)).perform(click())
    }



    override fun checkDefaultLayout() {
//        onView(withId(contentLayout)).check(matches(isDisplayed()))
//        onView(withId(emailTv)).check(matches(isDisplayed()))
//        onView(withId(deleteButton)).check(matches(isDisplayed()))
//        onView(withId(signOutButton)).check(matches(isDisplayed()))
    }

}