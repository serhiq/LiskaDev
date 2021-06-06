package com.gmail.uia059466.liska.screen

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.TestUtils

class ListsScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.list_content

//    in recycler view
    private val title = R.id.title_tv
    private val description = R.id.description_tv



    private val menuSort = R.string.menu_sort

    private val list = R.id.list
    private val addFab = R.id.add_fab

    fun createListFromFab(){
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

    fun openList(i: Int): ListDetailScreen {
        Thread.sleep(500)
        val recyclerView = onView(

            withId(R.id.list)

        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                i,
                click()
            )
        )
        Thread.sleep(200)
        return ListDetailScreen(device)


//        onView(withId(R.id.list)).perform(
//            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                0,
//                click()
//            )
//        )
    }

    fun openNavigationDrawer():NavigationDrawerScreen{
        onView(ViewMatchers.withContentDescription("open"))
            .perform(click())
        val screen=NavigationDrawerScreen(device)
        screen.waitForScreenToBeDisplayed()
        return screen
    }

    fun isDisplayedSnackbar(@StringRes emptyListDeleted: Int) {
        onView(withText(emptyListDeleted))
            .check(matches(isDisplayed())) }

    fun deleteList() {
        val menu_delete = R.string.menu_delete_list
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(menu_delete)).perform(click())
    }

    fun copyList() {
        val text = R.string.menu_copy_list
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(text)).perform(click())
    }



    fun checkItemInList(title: String,position:Int) {
            onView(TestUtils.withRecyclerView(R.id.list).atPositionOnView(position,R.id. title_tv)).check(
                ViewAssertions.matches(ViewMatchers.withText(title))
            )
    }

}