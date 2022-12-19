package com.jacobhyphenated.advent2022.day17

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun testPart1(){
        val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
        val day = Day17()
        val wind = day.parseInput(input)
        assertEquals(3068, day.part1(wind))
    }

    @Test
    fun testPart2(){
        val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
        val day = Day17()
        val wind = day.parseInput(input)
        assertEquals(1514285714288, day.part2(wind))
    }
}