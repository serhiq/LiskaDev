package com.gmail.uia059466.liska.domain.usecase

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


class CreateTitleWithCounterTest{

    @Test
    fun whenTitleCounterReturnTitleWithIncreseCounter(){
        val inputTitle ="Adumma"

        val result= CreateTitleWithCounter.createNextTitle(inputTitle)
        assertEquals("Adumma (2)",result)
    }
    @Test
    fun whenTitleCountdderReturnTitleWithIncreseCounter(){
        val inputTitle ="(12-12-23)"

        val result= CreateTitleWithCounter.createNextTitle(inputTitle)
        assertEquals("(12-12-23) (2)",result)
    }

    @Test
    fun whenTitleC5ountdderReturnTitleWithIncreseCounter(){
        val inputTitle ="(12-12-23) (2)"

        val result= CreateTitleWithCounter.createNextTitle(inputTitle)
        assertEquals("(12-12-23) (3)",result)
    }

    @Test
    fun wheddnTitleCounterReturnTitleWithIncreseCounter(){
        val inputTitle ="Adumma (4)"

        val result= CreateTitleWithCounter.createNextTitle(inputTitle)
        assertEquals("Adumma (5)",result)
    }
    @Test
    fun wheddnTitleCounterRetusrnTitleWithIncreseCounter(){
        val inputTitle ="12 -21-223-34"

        val result= CreateTitleWithCounter.createNextTitle(inputTitle)
        assertEquals("12 -21-223-34 (2)",result)
    }


    @Test
    fun whendTitleCounterReturnTitleWithIncreseCounter(){
        val inputTitle ="Adumma"
        val result= CreateTitleWithCounter.findNumberWithSq(inputTitle)
        assertNull(result)
    }

    @Test
    fun whendsTitleCounterReturnTitleWithIncreseCounter(){
        val inputTitle ="Adumma (1)"
        val result= CreateTitleWithCounter.findNumberWithSq(inputTitle)
        assertEquals(1,result)
    }
    @Test
    fun whendsTitlewCounterReturnTitleWithIncreseCounter(){
        val inputTitle ="Adumma (2)"
        val result= CreateTitleWithCounter.findNumberWithSq(inputTitle)
        assertEquals(2,result)
    }
    @Test
    fun whendsTitlewCounterRetu2rnTitleWithIncreseCounter(){
        val inputTitle ="Adumma (12)"
        val result= CreateTitleWithCounter.findNumberWithSq(inputTitle)
        assertEquals(null,result)
    }

        @Test
    fun whendsTitlewCounterRetdu2rnTitleWithIncreseCounter(){
        val inputTitle ="12 -21-223-34"
        val result= CreateTitleWithCounter.findNumberWithSq(inputTitle)
        assertEquals(null,result)
    }
    @Test
    fun whendsTeeitlewCounterRetdu2rnTitleWithIncreseCounter(){
        val inputTitle ="12 -21-223-34 (1)"
        val result= CreateTitleWithCounter.findNumberWithSq(inputTitle)
        assertEquals(1,result)
    }


}