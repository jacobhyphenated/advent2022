package com.jacobhyphenated.advent2022.day6

import com.jacobhyphenated.advent2022.Day

class Day6: Day<String> {
    override fun getInput(): String {
        return readInputFile("day6")
    }

    override fun warmup(input: String): Any {
        return findUniqueMarker(input, 2)
    }

    override fun part1(input: String): Number {
        return findUniqueMarker(input, 4)
    }

    override fun part2(input: String): Number {
        return findUniqueMarker(input, 14)
    }

    private fun findUniqueMarker(input: String, markerSize: Int): Number {
        val packet = input.toCharArray()
        for (index in markerSize until input.length) {
            if (packet.slice(index - markerSize until index).toSet().size == markerSize){
                return index
            }
        }
        return -1
    }
}