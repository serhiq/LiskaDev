package com.gmail.uia059466.liska

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.gmail.uia059466.liska.screen.SelectCatalogScreen
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import java.io.IOException
import java.util.concurrent.TimeoutException

abstract class BaseScreen(val device: UiDevice) {
 val id = "${BaseTest.pkg}:id"
  
  abstract val contentLayout:Int
  abstract fun checkDefaultLayout()

  fun waitForScreenToBeDisplayed(){
    waitForViewToBeDisplayed(id=contentLayout,milliseconds = 3_000)
  }
  
  fun isShow(): Boolean {
    return try {
      Espresso
              .onView(ViewMatchers.withId(contentLayout))
              .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
      true
    } catch (e: Throwable) {
      false
    }
  }
  
  
  fun isShow(strId:Int): Boolean {
    return try {
      Espresso
              .onView(ViewMatchers.withText(strId))
              .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
      true
    } catch (e: Throwable) {
      false
    }
  }
  
  fun string(res: Int): String = InstrumentationRegistry.getInstrumentation().targetContext.getString(
          res
                                                                                                     )
  fun isViewDisplayed(view: ViewInteraction): Boolean {
        return try {
            view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            true
        } catch (e: Throwable) {
            false
        }
    }
    
   fun getViewById(id: Int): ViewInteraction? {
        return Espresso.onView(Matchers.allOf(ViewMatchers.withId(id), ViewMatchers.isDisplayed()))
    }
  
  // Modified from https://stackoverflow.com/a/49814995/809944
   fun waitForViewToBeDisplayed(matcher: Matcher<View>, milliseconds: Long): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isDisplayed()
      }
      
      override fun getDescription(): String {
        return "Wait for a view matching the given matcher for $milliseconds millis."
      }
      
      override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + milliseconds
        do {
          for (child in TreeIterables.breadthFirstViewTraversal(view)) {
            // found matching view
            if (matcher.matches(child)) {
              return
            }
          }
          uiController.loopMainThreadForAtLeast(50)
        } while (System.currentTimeMillis() < endTime)
        throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
      }
    }
  }
  
  fun waitForViewMatching(matcher: Matcher<View>, milliseconds: Long) {
    Espresso
            .onView(ViewMatchers.isRoot())
            .perform(
                    waitForViewToBeDisplayed(
                            matcher,
                            milliseconds
                                            )
                    )
  }
  
  // Thanks to https://stackoverflow.com/a/49814995/809944
  fun waitForViewToBeDisplayed(id: Int, milliseconds: Long): ViewAction? {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isDisplayed()
      }
      
      override fun getDescription(): String {
        return "Wait for a specific view with id <$id> for $milliseconds millis."
      }
      
      override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + milliseconds
        val viewMatcher = ViewMatchers.withId(id)
        do {
          for (child in TreeIterables.breadthFirstViewTraversal(view)) {
            // found view with required ID
            if (viewMatcher.matches(child)) {
              return
            }
          }
          uiController.loopMainThreadForAtLeast(50)
        } while (System.currentTimeMillis() < endTime)
        throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
      }
    }
  }
  
  fun checkViewDisplayed(resId: Int){
    Espresso
            .onView(ViewMatchers.withId(resId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
  
  }
  
  fun checkTextDisplayed(strId: Int){
    Espresso
            .onView(ViewMatchers.withText(strId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
  
  }

  fun isKeyboardOpen(): Boolean {
      val checkKeyboardCmd = "dumpsys input_method | grep mInputShown"

      try {
          return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
              .executeShellCommand(checkKeyboardCmd).contains("mInputShown=true")
      } catch (e: IOException) {
          throw RuntimeException("Keyboard check failed", e)
      }
  }
}
