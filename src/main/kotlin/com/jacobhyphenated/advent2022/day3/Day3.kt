package com.jacobhyphenated.advent2022.day3

import com.jacobhyphenated.advent2022.Day

class Day3: Day<List<String>> {

    private val  priorityMap = (
            ('a'..'z').zip(1..26) +
            ('A'..'Z').zip(27..52)
        ).toMap()

    override fun getInput(): List<String> {
        return readInputFile("day3").lines()
    }

    override fun part1(input: List<String>): Number {

        return input.sumOf {
            val half = it.length / 2
            val firstHalf = it.substring(0, half).toCharArray().toSet()
            val secondHalf = it.substring(half, it.length).toCharArray().toSet()
            firstHalf.intersect(secondHalf)
                .sumOf { c -> priorityMap.getValue(c) }
        }
    }

    override fun part2(input: List<String>): Number {
        return input.windowed(3, 3).sumOf { rucksack ->
            rucksack.map { it.toCharArray().toSet() }
                .reduce { a, b -> a.intersect(b) }
                .sumOf { priorityMap.getValue(it) }
        }
    }
}