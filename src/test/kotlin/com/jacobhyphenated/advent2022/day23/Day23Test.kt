package com.jacobhyphenated.advent2022.day23

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day23Test {

    @Test
    fun testPart1() {
        val input = """
            ....#..
            ..###.#
            #...#.#
            .#...##
            #.###..
            ##.#.##
            .#..#..
        """.trimIndent()
        val day = Day23()
        val elves = day.parseInput(input)
        assertEquals(110, day.part1(elves))
    }

    @Test
    fun testPart2() {
        val input = """
            ....#..
            ..###.#
            #...#.#
            .#...##
            #.###..
            ##.#.##
            .#..#..
        """.trimIndent()
        val day = Day23()
        val elves = day.parseInput(input)
        assertEquals(20, day.part2(elves))
    }
}