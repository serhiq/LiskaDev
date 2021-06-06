package com.gmail.uia059466.liska.screen

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.BaseScreen
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import java.lang.Thread.sleep

class RenameListScreen(on: UiDevice) : BaseScreen(on) {
    override val contentLayout: Int= R.id.editText
    override fun checkDefaultLayout() {
        TODO("Not yet implemented")
    }

    fun replaceText(text: String) {
        onView(ViewMatchers.withId(editText)).perform(clearText())
        sleep(200)


        onView(ViewMatchers.withId(editText)).perform(typeText(text))
    }

    fun clickOk() {
        onView(ViewMatchers.withText(okButton)).perform(click())
    }

    private val title = R.string.list_name_dialog
    private val editText = R.id.editText
    private val okButton = android.R.string.ok



    companion object{
        fun createAndWait(device:UiDevice):RenameListScreen{
            val screen=RenameListScreen(device)
            screen.waitForScreenToBeDisplayed()
            return screen
        }
    }


}