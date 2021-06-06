package com.gmail.uia059466.liska.feature.setting

import com.gmail.uia059466.liska.BaseTest
import com.gmail.uia059466.liska.RotoDevice
import junit.framework.Assert.assertTrue
import org.junit.Test

class OpenSortCatalog: BaseTest() {
    @Test
    fun openSortDialog_52(){
//      Given
        val screen= RotoDevice(device).openSetting()

//      When
       val dialog= screen.openCatalogSortDialog()
       assertTrue(dialog.isShow())
        device.pressBack()

//      Then
        screen.isShow()
    }
}