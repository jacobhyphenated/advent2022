package com.jacobhyphenated.advent2022.day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {
    private val  input = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent()

    @Test
    fun testPart1() {
        val day = Day13()
        val packets = day.parseInput(input)
        assertEquals(13, day.part1(packets))
    }

    @Test
    fun testPart2() {
        val day = Day13()
        val packets = day.parseInput(input)
        assertEquals(140, day.part2(packets))
    }
}