package com.jacobhyphenated.advent2022.day20

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20Test {

    @Test
    fun testPart1() {
        val list = listOf(1L,2,-3,3,-2,0,4)
        val day = Day20()
        assertEquals(3, day.part1(list))
    }

    @Test
    fun testPart2() {
        val list = listOf(1L,2,-3,3,-2,0,4)
        val day = Day20()
        assertEquals(1623178306, day.part2(list))
    }
}