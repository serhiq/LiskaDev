package com.gmail.uia059466.liska.data.database

import org.junit.Test

import org.junit.Assert.*

class ListDatabaseKtTest {

    /* When

    если пусто внутри - пустое значение описание
    если один пункт  - пункт
"\u2713"
     */

//    fun createVeryLongDescription(items: List<ListItem>): String {
    @Test
    fun whenEmptyThenEmpty() {
        val li= emptyList<ListItem>()
        val result = createVeryLongDescription(li)
        assertEquals("",result)
    }

    @Test
    fun whenOneItemInListThenOneRow() {
        val li= listOf<ListItem>(ListItem("one",isChecked = false))
        val result = createVeryLongDescription(li)
        assertEquals(" \u2610 one",result)
    }

    @Test
    fun whenTwoToTenItemInListThenMaxRow() {
        val li= listOf<ListItem>(ListItem("one",isChecked = false),ListItem("two",isChecked = false))
        val result = createVeryLongDescription(li)
        assertEquals(" \u2610 one\n ☐ two",result)
    }

    @Test
    fun whenMoreThenTenThenThreeDotInEnd() {
        val itemRepeat=ListItem(title = "one")
        val li= listOf<ListItem>(itemRepeat,itemRepeat,itemRepeat,itemRepeat,itemRepeat,itemRepeat,itemRepeat,itemRepeat,itemRepeat,itemRepeat,itemRepeat)
        val result = createVeryLongDescription(li)
        assertEquals(" \u2610 one\n ☐ one\n ☐ one\n ☐ one\n ☐ one\n ☐ one\n ☐ one\n ☐ one\n ☐ one\n ☐ one\n   \u2026",result)
    }
}