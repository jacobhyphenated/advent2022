package com.jacobhyphenated.advent2022.day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14Test {

    @Test
    fun testPart1() {
        val input = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimIndent()
        val rocks = input.lines().map { line ->
            line.split(" -> ").map {
                val (x,y) = it.split(",")
                Pair(x.toInt(),y.toInt())
            }
        }
        val day = Day14()
        assertEquals(24, day.part1(day.buildRockStructure(rocks)))
    }

    @Test
    fun testPart2() {
        val input = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimIndent()
        val rocks = input.lines().map { line ->
            line.split(" -> ").map {
                val (x,y) = it.split(",")
                Pair(x.toInt(),y.toInt())
            }
        }
        val day = Day14()
        assertEquals(93, day.part2(day.buildRockStructure(rocks)))
    }
}