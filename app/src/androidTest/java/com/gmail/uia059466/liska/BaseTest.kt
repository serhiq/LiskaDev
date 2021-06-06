package com.gmail.uia059466.liska

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.jraska.falcon.FalconSpoonRule
import com.squareup.spoon.SpoonRule
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith


@Ignore("Ignoring BaseTest")
@RunWith(AndroidJUnit4::class)
abstract class BaseTest {


    lateinit var device: UiDevice
   
    @get:Rule
    val falconSpoonRule = FalconSpoonRule()
    
    @get:Rule
    val spoon = SpoonRule()
    
    @Before
    fun setUp() {
        device = startDevice()
    }
    
    private fun startDevice(): UiDevice {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()
    
        // Wait for launcher
        val launcherPackage: String = getLauncherPackageName()
        Assert.assertThat(launcherPackage, CoreMatchers.notNullValue())
        device.wait<Boolean>(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT.toLong()
        )
        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(Companion.pkg)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
        context.startActivity(intent)
   
        // Wait for the app to appear
        device.wait<Boolean>(
            Until.hasObject(
                By.pkg(Companion.pkg)
                    .depth(0)
            ), LAUNCH_TIMEOUT
        )
    
        screenShot("on_start")
        return device
    }

    @Rule
    @JvmField
    var watcher = object : TestWatcher() {
        override fun failed(e: Throwable, description: Description) {
            val tag="on_failed"+ description.methodName
//            screenShot(tag)
        }

        override fun succeeded(description: Description) {
//            val tag="on_finish"+description.methodName
//            screenShot(tag)
        }
    }
    
    val activity: Activity?
        get() {
            var activity: Activity? = null
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                activity = ActivityLifecycleMonitorRegistry
                        .getInstance()
                        .getActivitiesInStage(Stage.RESUMED)
                        .first()
                
            }
            return activity
        }
    
    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    fun getLauncherPackageName(): String {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        
        val pm = ApplicationProvider.getApplicationContext<Context>().packageManager
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo!!.activityInfo.packageName
    }
    
    fun screenShot(tag: String){
        falconSpoonRule.screenshot(activity, tag)
    }
    
    fun sleep(millis: Long){
        Thread.sleep(millis)
    }
   
    companion object {
        private const val LAUNCH_TIMEOUT = 5_000L
        const val pkg            = "com.gmail.uia059466.liska"
    }
}