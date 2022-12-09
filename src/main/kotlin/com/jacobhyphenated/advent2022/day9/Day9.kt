package com.jacobhyphenated.advent2022.day9

import com.jacobhyphenated.advent2022.Day
import kotlin.math.absoluteValue

/**
 * Day 9: Rope Bridge
 *
 * Knots on a rope follow a particular pattern that can be represented in 2d space.
 * The tail knot must always be adjacent (including diagonals) to the head knot.
 * As the head moves, the tail follows:
 *      ...   ->   ...   ->   ...
 *      TH.        T.H        .TH
 * and
 *      ...   ->   .H.   ->   .H.
 *      .H.        ...        .T.
 *      T..        T..        ...
 */
class Day9: Day<List<Pair<Direction, Int>>> {
    override fun getInput(): List<Pair<Direction, Int>> {
        return parseInput(readInputFile("day9"))
    }

    override fun warmup(input: List<Pair<Direction, Int>>): Int {
        return part1(input) + part2(input)
    }

    /**
     * Given the input instructions which describe how the head knot moves,
     * How many unique spaces does the tail knot visit?
     */
    override fun part1(input: List<Pair<Direction, Int>>): Int {
        var head = Pair(0,0)
        var tail = Pair(0,0)
        val visited = mutableSetOf(Pair(0,0))
        input.forEach { (direction, distance) ->
            repeat(distance) {
                head = updatePosition(head, direction)
                tail = calcNewTailPosition(head, tail)
                visited.add(tail)
            }
        }
        return visited.size
    }

    /**
     * The rope actually has 10 knots. The head knot moves as before.
     * Each knot in the rope acts like the tail knot to the knot in front of it.
     * How many unique spaces does the last tail know move through?
     */
    override fun part2(input: List<Pair<Direction, Int>>): Int {
        val rope = MutableList(10) { Pair(0,0) }
        val visited = mutableSetOf(Pair(0,0))
        input.forEach { (direction, distance) ->
            repeat(distance) {
                rope[0] = updatePosition(rope[0], direction)
                for (i in 1 until rope.size) {
                    rope[i] = calcNewTailPosition(rope[i-1], rope[i])
                }
                visited.add(rope.last())
            }
        }
        return visited.size

    }

    private fun updatePosition(position: Pair<Int,Int>, direction: Direction): Pair<Int,Int> {
        val (x,y) = position
        return when (direction) {
            Direction.DOWN -> Pair(x, y - 1)
            Direction.UP -> Pair(x, y + 1)
            Direction.LEFT -> Pair(x - 1, y)
            Direction.RIGHT -> Pair(x + 1, y)
        }
    }

    private fun calcNewTailPosition(head: Pair<Int,Int>, tail: Pair<Int,Int>): Pair<Int,Int> {
        // First check to see if the tail needs to be updated at all
        if ((head.first - tail.first).absoluteValue <= 1 && (head.second - tail.second) .absoluteValue <= 1) {
            return tail
        }

        return  if (head.first == tail.first) {
            // They have the same x coordinate, only adjust y
            Pair(tail.first, tail.second + if (tail.second > head.second) { -1 } else { 1 })
        } else if (head.second == tail.second) {
            // They have the same y coordinate, only adjust x
            Pair(tail.first + if (tail.first > head.first) { -1 } else { 1 }, tail.second)
        } else {
            // Account for diagonals
            val newX = tail.first + if (head.first > tail.first) { 1 } else { -1 }
            val newY = tail.second + if (head.second > tail.second) { 1 } else { -1 }
            Pair(newX, newY)
        }
    }

    fun parseInput(input: String): List<Pair<Direction,Int>> {
        return input.lines().map { line ->
            val (dir, num) = line.trim().split(" ")
            val direction = when(dir) {
                "R" -> Direction.RIGHT
                "U" -> Direction.UP
                "L" -> Direction.LEFT
                "D" -> Direction.DOWN
                else -> throw NotImplementedError("Invalid input: $dir")
            }
            Pair(direction, num.toInt())
        }
    }
}

enum class Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
}