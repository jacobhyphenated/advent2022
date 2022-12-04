package com.jacobhyphenated.advent2022.day4

import com.jacobhyphenated.advent2022.Day

// Use type aliases to avoid repeatedly writing a bunch of Pair<> types
typealias Section = Pair<Int,Int>
typealias SectionGroup = Pair<Section, Section>

/**
 * Day 4: Camp Cleanup
 *
 * Each group of two elves is assigned 2 sections to clean. Sometimes those sections overlap.
 *
 * Each group is represented by two "sections" which are Pair<Int,Int>
 * The pair's first value is the low value and the second is the high value of the range covered by the section
 */
class Day4: Day<List<SectionGroup>> {
    override fun getInput(): List<SectionGroup> {
        return readInputFile("day4").lines()
            .map {
                val (group1, group2) = it
                    .split(",")
                    .map { range ->
                        val (low, high) = range.split("-").map { num -> num.toInt() }
                        Pair(low, high)
                    }
                Pair(group1, group2)
            }
    }

    /**
     * Find groups where one section is completely inside the other section
     * Return the count of groups that have such overlap
     */
    override fun part1(input: List<SectionGroup>): Number {
        return input.count {
            val (group1, group2) = it
            // A section is contained inside another if:
            //      The low value is greater than or equal to the other low and
            //      the high value is less than or equal to the other high value
            // Do this in both directions
            (group1.first >= group2.first && group1.second <= group2.second) ||
                (group2.first >= group1.first && group2.second <= group1.second)
        }
    }

    /**
     * Find groups where there is any overlap at all and return the count of those groups.
     */
    override fun part2(input: List<SectionGroup>): Number {
        return input.count {
            val (group1, group2) = it
            // Similar to part one, except we only need to check if the low value is between the other's low and high
            (group1.first >= group2.first && group1.first <= group2.second) ||
                (group2.first >= group1.first && group2.first <= group1.second)
        }
    }
}
