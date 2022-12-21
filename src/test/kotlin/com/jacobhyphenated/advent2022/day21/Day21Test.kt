package com.jacobhyphenated.advent2022.day21

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21Test {

    val input = """
        root: pppw + sjmn
        dbpl: 5
        cczh: sllz + lgvd
        zczc: 2
        ptdq: humn - dvpt
        dvpt: 3
        lfqf: 4
        humn: 5
        ljgn: 2
        sjmn: drzm * dbpl
        sllz: 4
        pppw: cczh / lfqf
        lgvd: ljgn * ptdq
        drzm: hmdt - zczc
        hmdt: 32
    """.trimIndent()

    @Test
    fun testPart1() {
        val day = Day21()
        val monkey = day.parseInput(input)
        assertEquals(152, day.part1(monkey))
    }

    @Test
    fun testPart2() {
        val day = Day21()
        val monkey = day.parseInput(input)
        assertEquals(301, day.part2(monkey))
    }
}