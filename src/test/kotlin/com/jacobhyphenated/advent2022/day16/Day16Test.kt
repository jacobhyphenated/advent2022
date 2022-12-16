package com.jacobhyphenated.advent2022.day16

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test {

    private val input = """
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        Valve BB has flow rate=13; tunnels lead to valves CC, AA
        Valve CC has flow rate=2; tunnels lead to valves DD, BB
        Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
        Valve EE has flow rate=3; tunnels lead to valves FF, DD
        Valve FF has flow rate=0; tunnels lead to valves EE, GG
        Valve GG has flow rate=0; tunnels lead to valves FF, HH
        Valve HH has flow rate=22; tunnel leads to valve GG
        Valve II has flow rate=0; tunnels lead to valves AA, JJ
        Valve JJ has flow rate=21; tunnel leads to valve II
    """.trimIndent().lines()

    @Test
    fun testPart1() {
        val day = Day16()
        assertEquals(1651, day.part1(input))
    }

    @Test
    fun testPart2() {
        val day = Day16()
        assertEquals(1707, day.part2(input))
    }
}