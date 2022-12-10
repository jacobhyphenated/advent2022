package com.jacobhyphenated.advent2022.day10

import com.jacobhyphenated.advent2022.Day

/**
 * Day 10: Cathode-Ray Tube
 *
 * The input is a list of code instructions that are either:
 *      noop
 *      addx y
 * There is 1 register and a cpu clock. The register starts at 1
 * Noop takes 1 cpu cycle. Addx takes 2 cpu cycles
 * At the *end* of the cpu cycles needed for the instruction, the register is updated with the value in addx
 */
class Day10: Day<List<String>> {
    override fun getInput(): List<String> {
        return readInputFile("day10").lines()
    }

    /**
     * Signal strength is the value in the register multiplied by the clock time.
     * Add the signal strengths during the 20th, 60th, 100th, 140th, 180th, and 220th cycles.
     */
    override fun part1(input: List<String>): Int {
        val cpuMap = generateCpuRegisterMap(input)
        return (20 .. 220 step 40).sumOf { cpuMap.getValue(it) * it }
    }

    /**
     * The pixels are written to the screen based on the cpu time.
     * A single line is 40 pixels for every 40 cycles. There are 6 lines (240 cpu cycles)
     *
     * The register value is a pixel that occupies 3 spaces (register +- 1)
     * If the cpu cycle (mod 40 per line) is within the register pixel range, the pixel is turned on ("#")
     * Otherwise the pixel is off (".")
     *
     * Print the result on the screen (should be 8 capital letters)
     */
    override fun part2(input: List<String>): String {
        val cpuMap = generateCpuRegisterMap(input)
        return (0 until 240).windowed(40, step = 40).joinToString("\n", prefix = "\n") { lineRange ->
            lineRange.joinToString("") { cpuTime ->
                val position = cpuTime % 40
                val register = cpuMap.getValue(cpuTime + 1) //1 index vs 0 index position
                if (position in register - 1..register + 1) {
                    "#"
                } else {
                    "."
                }
            }
        }
    }

    /**
     * generate a map that shows the register value at every cpu clock cycle
     */
    private fun generateCpuRegisterMap(input: List<String>): Map<Int,Int> {
        var register = 1
        var clock = 1
        val cpuMap = mutableMapOf(clock to register)
        input.forEach { instruction ->
            if (instruction == "noop") {
                clock++
            } else { // addx
                val (_, value) = instruction.split(" ")
                cpuMap[clock + 1] = register
                register += value.toInt()
                clock += 2
            }
            cpuMap[clock] = register
        }
        return cpuMap
    }

}