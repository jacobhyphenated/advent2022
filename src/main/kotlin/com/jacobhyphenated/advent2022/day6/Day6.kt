package com.jacobhyphenated.advent2022.day6

import com.jacobhyphenated.advent2022.Day

/**
 * Day 6: Tuning Trouble
 *
 * The malfunctioning communicator receives characters in order (puzzle input)
 * These characters make up a packet of information.
 */
class Day6: Day<String> {
    override fun getInput(): String {
        return readInputFile("day6")
    }

    override fun warmup(input: String): Any {
        // teach the compiler how to optimize for this function
        return findUniqueMarker(input, 2)
    }

    /**
     * The packet start is denoted by 4 different consecutive characters.
     * How many characters have arrived when the packet start is identified?
     */
    override fun part1(input: String): Number {
        return findUniqueMarker(input, 4)
    }

    /**
     * The packet message consists of 14 unique consecutive characters
     * How many characters have arrived before you can read the packet message?
     */
    override fun part2(input: String): Number {
        return findUniqueMarker(input, 14)
    }

    /**
     * Both parts are solved with the same algorithm.
     * Use a marker size to look at a moving window of characters
     * If a set of those characters is equal to the marker size, all characters are unique
     *
     * indexOfFirst gives the index at the beginning of the window. Add the marker size
     * to find the total number of characters in the packet so far
     */
    private fun findUniqueMarker(input: String, markerSize: Int): Number {
        return markerSize + input.toList()
            .windowed(markerSize)
            .indexOfFirst { it.toSet().size == markerSize }
    }
}