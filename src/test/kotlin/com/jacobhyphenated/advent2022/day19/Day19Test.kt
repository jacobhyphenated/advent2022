package com.jacobhyphenated.advent2022.day19

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {

    private val input = """
        Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
        Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent()

    @Test
    fun testPart1() {
        val day = Day19()
        val blueprints = day.parseInput(input)
        assertEquals(33, day.part1(blueprints))
    }
}