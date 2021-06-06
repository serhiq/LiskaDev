package com.gmail.uia059466.liska.screen

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R

class NavigationDrawerScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.navigation_title_tv
    private val settingImg = R.id.setting_img
    private val nav_lists = R.id.nav_lists
    private val nav_catalog = R.id.nav_catalog
    private val nav_add_list = R.id.nav_add_list

    override fun checkDefaultLayout() {
//        onView(withId(contentLayout)).check(matches(isDisplayed()))
//        onView(withId(emailTv)).check(matches(isDisplayed()))
//        onView(withId(deleteButton)).check(matches(isDisplayed()))
//        onView(withId(signOutButton)).check(matches(isDisplayed()))
    }

    fun openSetting(): SettingScreen {
        Espresso.onView(ViewMatchers.withId(settingImg)).perform(ViewActions.click())
        val settingScreen=SettingScreen(device)
        settingScreen.waitForScreenToBeDisplayed()
        return settingScreen
    }

    fun openCatalog(): CatalogScreen {
        Espresso.onView(ViewMatchers.withId(nav_catalog)).perform(ViewActions.click())
        val settingScreen=CatalogScreen(device)
        settingScreen.waitForScreenToBeDisplayed()
        return settingScreen
    }

    fun createList(): ListDetailScreen {
        Espresso.onView(ViewMatchers.withId(nav_add_list)).perform(ViewActions.click())
        val screen=ListDetailScreen(device)
        screen.waitForScreenToBeDisplayed()
        return screen
    }

    fun openLists(): ListsScreen {
        Espresso.onView(ViewMatchers.withId(nav_lists)).perform(ViewActions.click())
        val screen=ListsScreen(device)
        screen.waitForScreenToBeDisplayed()
        return screen
    }
}