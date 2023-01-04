package com.jacobhyphenated.advent2022.day25

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day25Test {

    @Test
    fun testSnafuToDecimal() {
        val day = Day25()
        assertEquals(1, day.snafuToDecimal("1"))
        assertEquals(2, day.snafuToDecimal("2"))
        assertEquals(3, day.snafuToDecimal("1="))
        assertEquals(4, day.snafuToDecimal("1-"))
        assertEquals(5, day.snafuToDecimal("10"))
        assertEquals(6, day.snafuToDecimal("11"))
        assertEquals(7, day.snafuToDecimal("12"))
        assertEquals(8, day.snafuToDecimal("2="))
        assertEquals(9, day.snafuToDecimal("2-"))
        assertEquals(10, day.snafuToDecimal("20"))
        assertEquals(15, day.snafuToDecimal("1=0"))
        assertEquals(20, day.snafuToDecimal("1-0"))
        assertEquals(2022, day.snafuToDecimal("1=11-2"))
        assertEquals(12345, day.snafuToDecimal("1-0---0"))
        assertEquals(314159265, day.snafuToDecimal("1121-1110-1=0"))
    }

    @Test
    fun testDecimalToSnafu() {
        val day = Day25()
        assertEquals("1=-0-2", day.decimalToSnafu(1747))
        assertEquals("12111", day.decimalToSnafu(906))
        assertEquals("21", day.decimalToSnafu(11))
        assertEquals("1=", day.decimalToSnafu(3))
        assertEquals("1=11-2", day.decimalToSnafu(2022))
        assertEquals("1121-1110-1=0", day.decimalToSnafu(314159265))
    }
}