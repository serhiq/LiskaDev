package com.gmail.uia059466.liska.screen

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R

class SettingScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.setting_container
    val feedback_ll=R.id.feedback_ll
    val favorite_units_rv=R.id.favorite_units_rv
    val sort_catalog_rv=R.id.sort_catalog_rv

    fun openFeedBack():FeedBackScreen{
        Espresso.onView(ViewMatchers.withId(feedback_ll)).perform(ViewActions.click())
        val feedBackScreen=FeedBackScreen(device)
        feedBackScreen.waitForScreenToBeDisplayed()
        return feedBackScreen
    }



    override fun checkDefaultLayout() {
//        onView(withId(contentLayout)).check(matches(isDisplayed()))
//        onView(withId(emailTv)).check(matches(isDisplayed()))
//        onView(withId(deleteButton)).check(matches(isDisplayed()))
//        onView(withId(signOutButton)).check(matches(isDisplayed()))
    }

    fun openFavUnits(): FavoriteUnitsScreen {
        Espresso.onView(ViewMatchers.withId(favorite_units_rv)).perform(ViewActions.click())
        val favoriteUnitsScreen=FavoriteUnitsScreen(device)
        favoriteUnitsScreen.waitForScreenToBeDisplayed()
        return favoriteUnitsScreen
    }

    fun openCatalogSortDialog():SelectSortDialogScreen {
        Espresso.onView(ViewMatchers.withId(sort_catalog_rv)).perform(ViewActions.click())
        val screen=SelectSortDialogScreen(device)
        screen.waitForScreenToBeDisplayed()
        return screen

    }
}