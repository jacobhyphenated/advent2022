package com.jacobhyphenated.advent2022.day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {

    @Test
    fun testPart1() {
        val input = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent()
            .lines().map { it.toCharArray().toList() }
        val day = Day12()
        assertEquals(31, day.part1(input))
    }
}