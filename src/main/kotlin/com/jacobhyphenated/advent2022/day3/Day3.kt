package com.jacobhyphenated.advent2022.day3

import com.jacobhyphenated.advent2022.Day

/**
 * Day 3: Rucksack Reorganization
 *
 * There are a bunch of rucksacks with different items (characters a-z and A-Z)
 * Each item has a priority: a-z is 1-26 and A-Z is 27-52
 */
class Day3: Day<List<String>> {

    // create a map that ties each item character to its priority score
    private val  priorityMap = (
            ('a'..'z').zip(1..26) +
            ('A'..'Z').zip(27..52)
        ).toMap()

    override fun getInput(): List<String> {
        return readInputFile("day3").lines()
    }

    /**
     * Each rucksack is evenly split into two sections.
     * For each rucksack, the two sections will have 1 item in common.
     * Find that item's priority and sum the priority score for all rucksacks.
     */
    override fun part1(input: List<String>): Number {
        return input.sumOf {
            val half = it.length / 2
            val firstHalf = it.substring(0, half).toCharArray().toSet()
            val secondHalf = it.substring(half, it.length).toCharArray().toSet()
            firstHalf.intersect(secondHalf)
                .sumOf { c -> priorityMap.getValue(c) }
        }
    }

    /**
     * The elves have groups of three (in order based on the puzzle input).
     * Each group has exactly one item in all 3 rucksacks.
     *
     * Find the priority of the unique item for each group, and add them all up.
     */
    override fun part2(input: List<String>): Number {
        // use windowed to get groups of 3 with a step of 3 between each group so no rucksack appears in multiple groups
        return input.windowed(3, 3).sumOf { rucksack ->
            rucksack.map { it.toCharArray().toSet() }
                .reduce { a, b -> a.intersect(b) }// set intersection to find the common item in rucksack group
                .sumOf { priorityMap.getValue(it) }
        }
    }
}