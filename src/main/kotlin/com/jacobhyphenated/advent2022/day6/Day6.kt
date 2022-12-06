package com.jacobhyphenated.advent2022.day6

import com.jacobhyphenated.advent2022.Day

class Day6: Day<String> {
    override fun getInput(): String {
        return readInputFile("day6")
    }

    override fun part1(input: String): Number {
        // starts the index of the packet marker window at 2+1 == 4th character
        var index = 2
        val marker = mutableSetOf<Char>()
        while (marker.size != 4) {
            index++
            marker.clear()
            marker.addAll(arrayOf(input[index], input[index-1], input[index-2], input[index-3]))
        }
        return index + 1
    }

    override fun part2(input: String): Number {
        val packet = input.toCharArray()
        var index = 12
        val marker = mutableSetOf<Char>()
        while (marker.size != 14) {
            index++
            marker.clear()
            marker.addAll(packet.slice(index - 13 .. index))
        }
        return index + 1
    }
}