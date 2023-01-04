package com.jacobhyphenated.advent2022.day25

import com.jacobhyphenated.advent2022.Day
import kotlin.math.pow

/**
 * Day 25: Full of Hot Air
 *
 * A SNAFU number is a special kind of base 5 number that uses the characters
 * = (-2), - (-1), 0, 1, 2
 * therefore 3 is 1=
 * 9 is 2-
 * 2022 is 1=11-2
 */
class Day25: Day<List<String>> {
    override fun getInput(): List<String> {
        return readInputFile("day25").lines()
    }

    /**
     * The puzzle input is a list of SNAFU numbers.
     * Add the numbers up. Return the answer as a SNAFU number.
     */
    override fun part1(input: List<String>): String {
        val total = input.sumOf { snafuToDecimal(it) }
        return decimalToSnafu(total)
    }

    override fun part2(input: List<String>): String {
        return ""
    }

    /**
     * Decimal conversion is pretty straightforward
     * Each digit i (starting at 0 and counting from the right) is the digit * 5 ^ i
     * digits can be negative, sum all the digits.
     */
    fun snafuToDecimal(snafu: String): Long {
        return snafu.toList().map { when(it) {
            '0' -> 0
            '1' -> 1
            '2' -> 2
            '-' -> -1
            '=' -> -2
            else -> throw NotImplementedError("Invalid snafu digit $it")
        } }.reversed()
            .mapIndexed { i, digit -> digit * 5.toDouble().pow(i).toLong() }
            .sum()
    }

    fun decimalToSnafu(decimal: Long): String {
        var exp = 0
        // first, find the largest power of 5 that is still less than the decimal number
        while (5.0.pow(exp).toLong() < decimal) {
            exp++
        }
        // pre-fill an array of digits (leave 1 extra space at the beginning for overflow)
        val digits = MutableList(exp+1) { 0 }
        exp -= 1

        // Fill in each digit by dividing by the appropriate power of 5. digits can be 0 .. 4
        var remainder = decimal
        for (exponent in exp downTo 1) {
            val power = 5.0.pow(exponent).toLong()
            val rawDigit = remainder / power
            remainder %= power
            digits[exponent] = rawDigit.toInt()
        }
        digits[0] = remainder.toInt()

        // Now we do a pass from the least significant digit to the greatest
        // convert 3,4, or 5 to the appropriate snafu characters (which can carry over to the next digit)
        for (i in 0 until digits.size) {
            if (digits[i] == 3) {
                digits[i] = -2
                digits[i+1] += 1
            }
            else if (digits[i] == 4) {
                digits[i] = -1
                digits[i+1] += 1
            }
            else if (digits[i] == 5) {
                digits[i] = 0
                digits[i+1] += 1
            }
        }

        // convert to string and replace negatives with special characters
        return digits.map { when(it) {
            -2 -> "="
            -1 -> "-"
            else -> it
        } }.reversed().joinToString("").trimStart('0')
    }
}