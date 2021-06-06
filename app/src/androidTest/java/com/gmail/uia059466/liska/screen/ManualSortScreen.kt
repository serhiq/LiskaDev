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

class ManualSortScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.list_content

//    in recycler view
    private val title = R.id.title_tv
    private val description = R.id.description_tv



    private val menuSort = R.string.menu_sort

    private val list = R.id.list
    private val addFab = R.id.add_fab

    fun createList(){
        Espresso.onView(ViewMatchers.withId(addFab)).perform(ViewActions.click())
//        val new=SignUpScreen(device)
//        new.waitForScreenToBeDisplayed()
//        return new

    }

    fun openSortList(): SelectSortDialogScreen {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(menuSort)).perform(click())
        val new=SelectSortDialogScreen(device)
        new.waitForScreenToBeDisplayed()
        return new

    }

    override fun checkDefaultLayout() {
//        onView(withId(contentLayout)).check(matches(isDisplayed()))
//        onView(withId(emailTv)).check(matches(isDisplayed()))
//        onView(withId(deleteButton)).check(matches(isDisplayed()))
//        onView(withId(signOutButton)).check(matches(isDisplayed()))
    }
}