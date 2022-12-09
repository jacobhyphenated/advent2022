package com.jacobhyphenated.advent2022.day9

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day9Test {

    @Test
    fun testPart1() {
        val inputString = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent()
        val day = Day9()
        val input = day.parseInput(inputString)
        assertEquals(13, day.part1(input))
    }

    @Test
    fun testPart2() {
        val inputString = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
        """.trimIndent()
        val day = Day9()
        val input = day.parseInput(inputString)
        assertEquals(36, day.part2(input))
    }
}