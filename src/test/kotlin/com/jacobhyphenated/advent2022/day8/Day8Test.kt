package com.jacobhyphenated.advent2022.day8

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day8Test {

    @Test
    fun testPart1() {
        val input = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent()
            .lines()
            .map { line -> line.toCharArray().map { it.digitToInt() } }

        val day = Day8()
        assertEquals(21, day.part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent()
            .lines()
            .map { line -> line.toCharArray().map { it.digitToInt() } }

        val day = Day8()
        assertEquals(8, day.part2(input))
    }

}