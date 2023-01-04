package com.jacobhyphenated.advent2022.day24

import com.jacobhyphenated.advent2022.Day
import java.util.PriorityQueue
import kotlin.math.absoluteValue

/**
 * Day 24: Blizzard Basin
 *
 * The puzzle input represents an area with open spaces and blizzards.
 * Each blizzard moves in a defined direction. When the blizzard reaches
 * the edge of the map, a new one starts on the opposite edge (same direction).
 * Multiple blizzards can occupy the same space.
 * Blizzards move one space per minute.
 *
 * Solutions are somewhat slow
 * Part 1 ~ 8 seconds
 * Part 2 ~ 26 seconds
 *
 * This is an improvement on my original DFS approach that took about 1 minute for part 1
 */
class Day24: Day<List<Blizzard>> {
    override fun getInput(): List<Blizzard> {
        return parseInput(readInputFile("day24"))
    }

    /**
     * Starting in the top left corner, get to the bottom left corner.
     * Find the fastest possible time to do this while never occupying the
     * same location as a blizzard.
     */
    override fun part1(input: List<Blizzard>): Int {
        val maxCol = input.maxOf { it.currentLocation.second }
        val maxRow = input.maxOf { it.currentLocation.first }
        return bfs(input, Pair(0,1), Pair(maxRow, maxCol), 0, maxRow, maxCol)
    }

    /**
     * Get from the start to the end. Then go back to the start to grab snacks.
     * Then go back to the end again.
     *
     * What is the best possible time to complete the journey in?
     */
    override fun part2(input: List<Blizzard>): Int {
        val blizzards = input.map { it.copy() }
        val maxCol = blizzards.maxOf { it.currentLocation.second }
        val maxRow = blizzards.maxOf { it.currentLocation.first }

        val timeAtEnd = bfs(input, Pair(0,1), Pair(maxRow, maxCol), 0, maxRow, maxCol)
        val timeToReturn = bfs(blizzards, Pair(maxRow+1, maxCol), Pair(1,1), timeAtEnd, maxRow, maxCol)
        return bfs(blizzards, Pair(0,1), Pair(maxRow, maxCol), timeToReturn, maxRow, maxCol)
    }

    /**
     * Solve using Breadth First Search.
     * A previous attempt using DFS was about 7 times slower.
     *
     * Find all viable paths, but optimize by focusing on states closest to the end location first.
     *
     * @param blizzards A list of all blizzards on the map
     * @param start the start location
     * @param end the end location - or rather, the location adjacent to the end location,
     * which is just outside the boundary rectangle
     * @param startMinute the time the bfs starts from the start location
     * @param maxRow the row boundary of the search rectangle: [1,maxRow]
     * @param maxCol the column boundary of the search rectangle: [1, maxCol]
     */
    private fun bfs(blizzards: List<Blizzard>,
                    start: Pair<Int, Int>,
                    end: Pair<Int,Int>,
                    startMinute: Int,
                    maxRow: Int,
                    maxCol: Int): Int {
        var bestSolution = 1000
        val visited = mutableSetOf<State>()
        // use a priority queue ordered by the minimum distance to the end location
        val queue = PriorityQueue<State> { a, b -> a.distance(end) - b.distance(end) }
        queue.add(State(start, startMinute))
        while(queue.isNotEmpty()) {
            val state = queue.remove()
            // We've already explored this state, defined by location and time
            if (state in visited) {
                continue
            }
            visited.add(state)

            // If we're at the end location, update the best solution time
            val (location, time) = state
            if (location == end) {
                if (time + 1 < bestSolution) {
                    bestSolution = time + 1
                }
                continue
            }

            // Imagine we can move from here to the end location without any blizzards
            // If we can't possibly beat our known best time, then we can stop searching this branch
            val (row, col) = location
            val (endRow, endCol) = end
            if ((endRow - row).absoluteValue + (endCol - col).absoluteValue + 1 + time > bestSolution) {
                continue
            }

            // Blizzard locations can be calculated based on the initial blizzard location and elapsed time
            val blizzardLocations = blizzards.map { it.locationAtTime(maxCol, maxRow, time + 1) }.toSet()

            // Explore the adjacent spaces within the boundary rectangle that will not have a blizzard in the next minute
            listOf(Pair(row - 1, col), Pair(row, col - 1), Pair(row, col + 1), Pair(row + 1, col))
                .filter { (r,c) ->  r >= 1 && r <= maxRow && c >= 1 && c <= maxCol }
                .filter { it !in blizzardLocations }
                .forEach { nextLocation ->
                    queue.add(State(nextLocation, time + 1))
                }
            // Also explore just staying in place and not moving this minute
            if (location !in blizzardLocations) {
                queue.add(State(location, time + 1))
            }
        }
        return bestSolution
    }

    fun parseInput(input: String): List<Blizzard> {
        return input.lines().flatMapIndexed { row, line -> line.mapIndexedNotNull { col, c ->
            when(c) {
                '>' -> Blizzard(Pair(row, col), Direction.RIGHT)
                '<' -> Blizzard(Pair(row, col), Direction.LEFT)
                '^' -> Blizzard(Pair(row, col), Direction.UP)
                'v' -> Blizzard(Pair(row, col), Direction.DOWN)
                else -> null
            }
        } }
    }
}

data class Blizzard(val currentLocation: Pair<Int,Int>, private val direction: Direction) {

    // We can tell where a blizzard will be at a given time using math
    fun locationAtTime(maxCol: Int, maxRow: Int, time: Int): Pair<Int,Int> {
        val (row,col) = currentLocation
        return when(direction) {
            Direction.UP ->  Pair((row - 1 - time).mod(maxRow) + 1, col)
            Direction.DOWN -> Pair((row - 1 + time).mod(maxRow) + 1, col)
            Direction.LEFT -> Pair(row, (col - 1 - time).mod(maxCol) + 1)
            Direction.RIGHT -> Pair(row, (col - 1 + time).mod(maxCol) + 1)
        }
    }
}

enum class Direction {
    DOWN, RIGHT, LEFT, UP
}

data class State(val location: Pair<Int,Int>, val time: Int) {
    fun distance(other: Pair<Int,Int>): Int {
        return (other.first - location.first).absoluteValue + (other.second - location.second).absoluteValue
    }
}