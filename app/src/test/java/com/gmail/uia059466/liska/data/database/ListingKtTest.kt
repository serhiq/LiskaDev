package com.gmail.uia059466.liska.data.database

import org.junit.Test

import org.junit.Assert.*

class ListingKtTest {

    @Test
    fun compareListItemEquals() {
//      Given
        val new = listOf(
            ListItem(title = "ONE", isChecked = false, isSelected = false),
            ListItem(title = "TWO", isChecked = false, isSelected = false))
        val old = listOf(
            ListItem(title = "ONE", isChecked = false, isSelected = false),
            ListItem(title = "TWO", isChecked = false, isSelected = false))
//      When

        val isEquals=listIsEquals(new,old)
//     Then
        assertTrue(isEquals)
    }

    @Test
    fun compareListItemEqualsSizeDiffTitle() {
//      Given
        val new = listOf(
            ListItem(title = "TWO", isChecked = false, isSelected = false),
            ListItem(title = "TWO", isChecked = false, isSelected = false))
        val old = listOf(
            ListItem(title = "ONE", isChecked = false, isSelected = false),
            ListItem(title = "TWO", isChecked = false, isSelected = false))
//      When

        val isEquals=listIsEquals(new,old)
//     Then
        assertFalse(isEquals)
    }

    @Test
    fun compareListItemEqualsSizeDiffChecked() {
//      Given
        val new = listOf(
            ListItem(title = "ONE", isChecked = true, isSelected = false),
            ListItem(title = "TWO", isChecked = false, isSelected = false))
        val old = listOf(
            ListItem(title = "ONE", isChecked = false, isSelected = false),
            ListItem(title = "TWO", isChecked = false, isSelected = false))
//      When

        val isDifferent=listIsEquals(new,old)
//     Then
        assertFalse(isDifferent)
    }
    @Test
    fun compareListItemEqualsSizeDiffSelected() {
//      Given
        val new = listOf(
            ListItem(title = "ONE", isChecked = false, isSelected = true),
            ListItem(title = "TWO", isChecked = false, isSelected = true))
        val old = listOf(
            ListItem(title = "ONE", isChecked = false, isSelected = false),
            ListItem(title = "TWO", isChecked = false, isSelected = false))
//      When

        val isDifferent=listIsEquals(new,old)
//     Then
        assertTrue(isDifferent)
    }



}