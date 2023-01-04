package com.jacobhyphenated.advent2022.day24

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day24Test {

    @Test
    fun testPart1() {
        val input = """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#
        """.trimIndent()
        val day = Day24()
        val blizzards = day.parseInput(input)
        assertEquals(18, day.part1(blizzards))
    }

    @Test
    fun testPart2() {
        val input = """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#
        """.trimIndent()
        val day = Day24()
        val blizzards = day.parseInput(input)
        assertEquals(54, day.part2(blizzards))
    }
}