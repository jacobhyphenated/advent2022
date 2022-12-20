package com.jacobhyphenated.advent2022.day18

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18Test {
    val input = listOf(
        Cube(2,2,2),
        Cube(1,2,2),
        Cube(3,2,2),
        Cube(2,1,2),
        Cube(2,3,2),
        Cube(2,2,1),
        Cube(2,2,3),
        Cube(2,2,4),
        Cube(2,2,6),
        Cube(1,2,5),
        Cube( 3,2,5),
        Cube(2,1,5),
        Cube(2,3,5)
    )


    @Test
    fun testPart2() {
        val day = Day18()
        assertEquals(58, day.part2(input))
    }
}