package com.gmail.uia059466.liska.screen

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.lists.sortorder.SortOrder

class FeedBackScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.feedback_et
    val editText: Int= R.id.feedback_et
    val menuSend: Int= R.id.menu_send_feedback
    //    in dialog

    val emptyMessageInDialog: Int= R.string.dialog_no_empty_feedback
    val positiveButtonInDialog=android.R.string.ok

    fun enterText(text:String){

    }

    fun pressSend(){

    }

    fun isDisplayDialog(){

    }



    override fun checkDefaultLayout() {
//        onView(withId(contentLayout)).check(matches(isDisplayed()))
//        onView(withId(emailTv)).check(matches(isDisplayed()))
//        onView(withId(deleteButton)).check(matches(isDisplayed()))
//        onView(withId(signOutButton)).check(matches(isDisplayed()))
    }

    fun clickOnHome() {
        onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    }
}