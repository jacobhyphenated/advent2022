package com.jacobhyphenated.advent2022.day1

import com.jacobhyphenated.advent2022.Day

/**
 * Day 1: Calorie Counting
 *
 * The elves packed snacks. Each elf tracks their own snacks by the number of calories.
 * The input is a list of Elves and their snack calorie counts represented as List<List<Int>>
 */
class Day1: Day<List<List<Int>>> {
    override fun getInput(): List<List<Int>> {
        return readInputFile("day1")
            .split("\n\n")
            .map {
                it.lines()
                .map { num -> num.toInt() }
            }
    }

    /**
     * Find the Elf carrying the most Calories. How many total Calories is that Elf carrying?
     */
    override fun part1(input: List<List<Int>>): Number {
        return input.maxOf { it.sum() }
    }

    /**
     * Find the top three Elves carrying the most Calories. How many Calories are those Elves carrying in total?
     */
    override fun part2(input: List<List<Int>>): Number {
        return input.map { it.sum() }
            .sortedDescending()
            .subList(0,3)
            .sum()
    }

}