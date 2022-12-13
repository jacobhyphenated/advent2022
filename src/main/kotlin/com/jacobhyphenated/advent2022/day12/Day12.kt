package com.jacobhyphenated.advent2022.day12

import com.jacobhyphenated.advent2022.Day
import java.util.PriorityQueue

/**
 * Day 12: Hill Climbing Algorithm
 *
 * The terrain is given as a 2d map of characters. 'S' is the start position, 'E' is the end position.
 * The characters indicate the height of the terrain with 'a' being the lowest and 'z' being the highest.
 * You can always climb down, but you can only climb up to a position 1 higher ( b -> c but not c -> e).
 * S is the equivalent of 'a' and E is the equivalent of 'z'.
 */
class Day12: Day<List<List<Char>>> {
    override fun getInput(): List<List<Char>> {
        return readInputFile("day12")
            .lines()
            .map { it.toCharArray().asList() }
    }

    /**
     * Find the path that takes the fewest number of steps to get from S to E
     */
    override fun part1(input: List<List<Char>>): Any {
        val startPosition = findStartPosition(input)
        return shortestPath(startPosition, input)
    }

    /**
     * In the future, we might want to create a trail.
     * Look at all 'a' locations as possible starting points.
     * Find the path that takes the fewest number of steps to get to E
     */
    override fun part2(input: List<List<Char>>): Int {
        val startingPositions = input.flatMapIndexed { r, row ->
            row.mapIndexed { index, row -> Pair(index, row) }
                .filter { (_, character) -> character == 'a' }
                .map { (c, _) -> Pair(r, c) }
        }
        return startingPositions.map { shortestPath(it, input) }
            .filter { it >= 0 } // Filter out unreachable paths
            .min()
    }

    /**
     * Dijkstra's algorithm to find the shortest path from a given starting point to the 'E' character.
     */
    private fun shortestPath(startPosition: Pair<Int,Int>, input: List<List<Char>>): Int {
        val distances: MutableMap<Pair<Int,Int>, Int> = mutableMapOf()
        // Use a priority queue implementation - min queue sorted by lowest "cost"
        val queue = PriorityQueue<PathCost> { a, b -> a.cost - b.cost }
        queue.add(PathCost(startPosition, 0))
        distances[startPosition] = 0
        var current: PathCost

        do {
            current = queue.remove()
            // If we already found a less expensive way to reach this position
            if (current.cost > (distances[current.location] ?: Int.MAX_VALUE)) {
                continue
            }
            val (r,c) = current.location
            // if this the end position, we've found the answer
            if (input[r][c] == 'E') {
                return current.cost
            }
            // From the current position, look in each direction for a valid move
            findAdjacent(r, c, input).forEach {
                // cost is the number of steps taken, increases by 1 for each move
                val cost = distances.getValue(current.location) + 1
                // If the cost to this space is less than what was previously known, put this on the queue
                if (cost < (distances[it] ?: Int.MAX_VALUE)) {
                    distances[it] = cost
                    queue.add(PathCost(it, cost))
                }
            }
        } while (queue.size > 0)
        return -1
    }

    // Get adjacent positions to the current position (no diagonals) that are reachable from the current position
    // a position is reachable if its height is not more than 1 greater than the current height
    private fun findAdjacent(row: Int, col: Int, grid: List<List<Char>>): List<Pair<Int,Int>> {
        val result = mutableListOf<Pair<Int,Int>>()
        for (r in (row - 1).coerceAtLeast(0) .. (row + 1).coerceAtMost(grid.size - 1)) {
            if (r == row) {
                continue
            }
            result.add(Pair(r, col))
        }
        for (c in (col - 1).coerceAtLeast(0) .. (col + 1).coerceAtMost(grid[row].size - 1)) {
            if (c == col) {
                continue
            }
            result.add(Pair(row, c))
        }
        val currentHeight = getCharHeight(grid[row][col])
        return result.filter { (r,c) -> getCharHeight(grid[r][c]) <= currentHeight + 1 }
    }

    private fun getCharHeight(c: Char): Int {
        if (c == 'S') {
            return 'a'.code
        }
        if (c == 'E') {
            return 'z'.code
        }
        return c.code
    }

    private fun findStartPosition(input: List<List<Char>>): Pair<Int,Int> {
        for (r in input.indices) {
            val c = input[r].indexOfFirst { it == 'S' }
            if (c >= 0) {
                return Pair(r,c)
            }
        }
        throw NotImplementedError("Could not find Start Position")
    }
}

data class PathCost(val location: Pair<Int,Int>, val cost: Int)