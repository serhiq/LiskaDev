package com.gmail.uia059466.liska.feat

import com.gmail.uia059466.liska.BaseTest
import com.gmail.uia059466.liska.RotoDevice
import junit.framework.Assert
import org.junit.Test

class FeedBackKeyboard: BaseTest() {
    @Test
//    bugfix 71

    fun keyBoardAlwaysShown(){
//      Given
        val screen= RotoDevice(device).openFeedback()

//      When
        sleep(500)

//      Then
        Assert.assertTrue(screen.isKeyboardOpen())
    }

    @Test
    fun keyBoardHideWhenBackOnPhone(){
//      Given
        val screen= RotoDevice(device).openFeedback()

//      When
        device.pressBack()
        sleep(500)

//      Then
        Assert.assertFalse(screen.isKeyboardOpen())
    }

    @Test
    fun keyBoardHideWhenBackOnTitle(){
//      Given
        val screen= RotoDevice(device).openFeedback()

//      When
        screen.clickOnHome()
        sleep(500)

//      Then
        Assert.assertFalse(screen.isKeyboardOpen())
    }
}