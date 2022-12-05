package com.jacobhyphenated.advent2022.day5

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day5Test {

    @Test
    fun testParseInput() {
        val input = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3 
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent()
        val day = Day5()
        val (crates, instructions) = day.parseInputString(input)
        assertEquals(3, crates.size)
        assertEquals('Z', crates[0][0])
        assertEquals(2, crates[0].size)
        assertEquals(listOf('M', 'C', 'D'), crates[1])
        assertEquals(listOf('P'), crates[2])

        assertEquals(4, instructions.size)
        assertEquals(Instruction(1,0, 1), instructions[0])
        assertEquals(2, instructions[1].destination)
        assertEquals(2, instructions[2].number)
    }

    @Test
    fun testPart1() {
        val inputString = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3 
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent()
        val day = Day5()
        val input = day.parseInputString(inputString)
        assertEquals("CMZ", day.part1(input))
    }

    @Test
    fun testPart2() {
        val inputString = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3 
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent()
        val day = Day5()
        val input = day.parseInputString(inputString)
        assertEquals("MCD", day.part2(input))
    }
}