package com.gmail.uia059466.liska

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gmail.uia059466.liska.domain.usecase.CreateTitleWithCounter
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CreateTitleWithCounterTest{

    @Test
    fun whenTitleCounterReturnTitleWithIncreseCounter(){
        val inputTitle ="Adumma"

        val result= CreateTitleWithCounter.createNextTitle(inputTitle)
        assertEquals("Adumma (2)",result)
    }

}