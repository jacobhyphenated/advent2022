package com.jacobhyphenated.advent2022.day22

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day22Test {

    val input = """
                ...#
                .#..
                #...
                ....
        ...#.......#
        ........#...
        ..#....#....
        ..........#.
                ...#....
                .....#..
                .#......
                ......#.
        
        10R5L5R10L4R5L5
    """.trimIndent()

    @Test
    fun testPart1() {
        val day = Day22()
        val puzzle = day.parseInput(input)
        assertEquals(6032, day.part1(puzzle))
    }
}