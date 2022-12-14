package com.jacobhyphenated.advent2022.day14

import com.jacobhyphenated.advent2022.Day
import kotlin.math.max
import kotlin.math.min

/**
 * Day 14: Regolith Reservoir
 *
 * Sand is dropping from the ceiling of the cave. The puzzle input is the rock structure of the cave in 2D.
 * The input shows lists of pairs that represent points in a continuous line.
 *
 * Sand falls from a point at (500,0) with every increase in the y-axes being the sand dropping by 1.
 */
class Day14: Day<List<List<Pair<Int,Int>>>> {

    override fun getInput(): List<List<Pair<Int, Int>>> {
        return readInputFile("day14").lines().map { line ->
            line.split(" -> ").map {
                val (x,y) = it.split(",")
                Pair(x.toInt(),y.toInt())
            }
        }
    }

    /**
     * Sand comes to rest against where the rock formations lines form.
     * If a piece of sand falls below the highest y value of the rock structure, it will fall forever.
     * Drop sand until one grain falls forever. How many grains of sand are dropped?
     */
    override fun part1(input: List<List<Pair<Int, Int>>>): Int {
        val rockStructure = buildRockStructure(input)
        val maxY = rockStructure.maxOf { (_,y) -> y }
        var sandCount = 0
        do {
            val sandLocation = dropSand(rockStructure, maxY)
            sandLocation?.also {
                sandCount++
                rockStructure.add(it)
            }
        } while (sandLocation != null)
        return sandCount
    }

    /**
     * There is an invisible floor that goes infinitely in the x direction.
     * It's 2 units beyond the highest y value of the rock structure.
     *
     * Drop sand until it covers the starting drop point (500,0)
     * How many grains of sand are dropped?
     */
    override fun part2(input: List<List<Pair<Int, Int>>>): Any {
        val rockStructure = buildRockStructure(input)
        val maxY = rockStructure.maxOf { (_,y) -> y }
        var sandCount = 0
        do {
            val sandLocation = dropSandWithBottom(rockStructure, maxY + 2)
            sandCount++
            rockStructure.add(sandLocation)
        } while (sandLocation != Pair(500,0))
        return sandCount
    }

    // Use the code from part2 to solve part1
    // return null if the sand falls forever (beyond maxY)
    private fun dropSand(rockStructure: Set<Pair<Int,Int>>, maxY: Int): Pair<Int,Int>? {
        val sand = dropSandWithBottom(rockStructure, maxY + 1)
        return if (sand.second < maxY) { sand } else { null }
    }

    /**
     * Let the sand fall until it hits a supplied bottom y-axis
     * Return the location where it comes to rest.
     *
     * If the sand can drop 1 y, it does.
     * If not, it tries to move diagonally to the left
     * If not, it tries to move diagonally to the right
     * otherwise, it comes to rest
     */
    private fun dropSandWithBottom(rockStructure: Set<Pair<Int, Int>>, bottom: Int): Pair<Int,Int> {
        // sand starts at 500,0
        var (x,y) = Pair(500, 0)
        while (y < bottom - 1){
            val (newX, newY) = listOf(Pair(x, y+1), Pair(x-1, y+1), Pair(x+1, y+1))
                .firstOrNull { !rockStructure.contains(it) }
                ?: return Pair(x,y)
            x = newX
            y = newY
        }
        return Pair(x,y)
    }

    /**
     * Represent the rocks as a set of all points that rocks occupy.
     * Go through each line segment and add all points to a set of rock locations
     */
    private fun buildRockStructure(input: List<List<Pair<Int,Int>>>): MutableSet<Pair<Int,Int>> {
        val set = mutableSetOf<Pair<Int,Int>>()
        for (rockSegment in input) {
            rockSegment.reduce { (x1, y1), (x2, y2) ->
                val rockLine = if (x1 == x2) {
                    (min(y1,y2)..max(y1,y2)).map { Pair(x1, it) }
                } else {
                    (min(x1,x2)..max(x1,x2)).map { Pair(it, y1) }
                }
                set.addAll(rockLine)
                Pair(x2,y2)
            }
        }
        return set
    }

}