package com.jacobhyphenated.advent2022.day2

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day2Test {
    @Test
    fun testPart1() {
        val input = """
            A Y
            B X
            C Z
        """.trimIndent().lines()
        val day = Day2()
        assertEquals(15, day.part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            A Y
            B X
            C Z
        """.trimIndent().lines()
        val day = Day2()
        assertEquals(12, day.part2(input))
    }
}