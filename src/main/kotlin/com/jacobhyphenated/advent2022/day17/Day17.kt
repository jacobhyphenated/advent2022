package com.jacobhyphenated.advent2022.day17

import com.jacobhyphenated.advent2022.Day

/**
 * Day 17: Pyroclastic Flow
 *
 * Rocks fall in a particular pattern (tetris style).
 * That pattern repeats forever.
 * The puzzle input shows what direction the wind will blow the falling rocks (also repeats).
 */
class Day17: Day<List<Direction>> {
    override fun getInput(): List<Direction> {
        return parseInput(readInputFile("day17"))
    }

    /**
     * Drop 2022 rocks. How tall is the rock structure?
     */
    override fun part1(input: List<Direction>): Int {
        val rockGenerator = InfiniteGenerator(
            listOf(RockType.H_LINE, RockType.PLUS, RockType.CORNER, RockType.V_LINE, RockType.SQUARE)
        )
        val windGenerator = InfiniteGenerator(input)

        var bottom = 0
        val allRocks = List(7) { Pair(it, bottom) }.toMutableSet()
        repeat(2022) {
            var rocks = createRockFormation(rockGenerator.next(), bottom)
            while (true) {
                // Read the wind and move the rock
                val wind = windGenerator.next()
                val next = moveRocks(rocks, wind)
                if (! next.any{ it.first < 0 || it.first >= 7 || it in allRocks }) {
                    // Only move the falling rocks if it stays within the bounds and doesn't touch an existing rock
                    rocks = next
                }
                // Move down iff it can safely fall another space
                val moveDown = moveRocks(rocks, Direction.DOWN)
                if (moveDown.any { it in allRocks }) {
                    allRocks.addAll(rocks)
                    bottom = allRocks.maxOf { (_, y) -> y }
                    break
                } else {
                  rocks = moveDown
                }
            }
        }
        return bottom
    }

    /**
     * How tall will the structure be after dropping 1,000,000,000,000 rocks
     */
    override fun part2(input: List<Direction>): Long {
        val rockGenerator = InfiniteGenerator(
            listOf(RockType.H_LINE, RockType.PLUS, RockType.CORNER, RockType.V_LINE, RockType.SQUARE)
        )
        val windGenerator = InfiniteGenerator(input)
        val rocksToDrop = 1_000_000_000_000

        var bottom = 0
        val allRocks = List(7) { Pair(it, bottom) }.toMutableSet()
        var rocksDropped = 0L
        var patternBaselineRocks: Long? = null
        var patternBaselineHeight: Int? = null
        var patternAddedHeight = 0L

        // start off similar to part 1, dropping rocks one at a time
        while(rocksDropped < rocksToDrop) {
            var rocks = createRockFormation(rockGenerator.next(), bottom)
            rocksDropped++
            while (true) {
                val wind = windGenerator.next()
                val next = moveRocks(rocks, wind)
                if (! next.any{ it.first < 0 || it.first >= 7 || it in allRocks }) {
                    rocks = next
                }

                // Look for patterns in the rock formation when we have a repeat in the wind pattern
                if (windGenerator.num % input.size == 0) {
                    if (patternBaselineHeight == null) {
                        patternBaselineHeight = bottom
                        patternBaselineRocks = rocksDropped
                    } else {
                        // For this puzzle input, the rock pattern repeats as the wind cycle repeats
                        // This is not guaranteed to be the case, and doesn't work for the test input
                        // I'm only solving for this specific puzzle input, not all possible puzzle inputs
                        val heightPattern = bottom - patternBaselineHeight
                        val rockPattern = rocksDropped - patternBaselineRocks!!

                        val rocksRemaining = rocksToDrop - rocksDropped
                        val patternRepeat = rocksRemaining / rockPattern

                        val effectiveBottom = findBottom(allRocks)
                        val normalizedRocks = normalize(allRocks, effectiveBottom)
                        allRocks.clear()
                        allRocks.addAll(normalizedRocks)
                        rocks = normalize(rocks, effectiveBottom)

                        // Once we know the pattern, calculate the height and rocks dropped
                        // we can skip ahead to the remaining rocks, which will manually fall as normal
                        patternAddedHeight = heightPattern * patternRepeat + effectiveBottom
                        rocksDropped += rockPattern * patternRepeat
                        bottom -= effectiveBottom
                    }
                }

                val moveDown = moveRocks(rocks, Direction.DOWN)
                if (moveDown.any { it in allRocks }) {
                    allRocks.addAll(rocks)
                    bottom = allRocks.maxOf { (_, y) -> y }
                    break
                } else {
                    rocks = moveDown
                }
            }

        }
        return patternAddedHeight + bottom

    }

    private fun moveRocks(rocks: List<Pair<Int,Int>>, direction: Direction): List<Pair<Int,Int>> {
        return when(direction) {
            Direction.DOWN -> rocks.map { (x,y) -> Pair(x, y - 1) }
            Direction.LEFT -> rocks.map { (x,y) -> Pair(x - 1, y) }
            Direction.RIGHT -> rocks.map { (x,y) -> Pair(x + 1 , y) }
        }
    }

    private fun findBottom(allRocks: Set<Pair<Int,Int>>): Int {
        val xValues = MutableList(7) { false }
        var topY = allRocks.maxOf { (_, y) -> y }
        while (!xValues.all { it }) {
            (0 .. 6).forEach { x ->
                if (Pair(x, topY) in allRocks) {
                    xValues[x] = true
                }
            }
            topY -= 1
        }
        return topY
    }

    private fun normalize(rocks: Collection<Pair<Int,Int>>, effectiveBottom: Int): List<Pair<Int,Int>> {
        return rocks
            .filter { (_,y) -> y >= effectiveBottom }
            .map { (x,y) -> Pair(x, y - effectiveBottom) }
    }

    private fun createRockFormation(type: RockType, bottom: Int): List<Pair<Int,Int>> {
        val bottomOffset = bottom + 4
        return when(type) {
            RockType.H_LINE -> listOf(Pair(2, bottomOffset), Pair(3, bottomOffset), Pair(4, bottomOffset), Pair(5,bottomOffset))
            RockType.PLUS -> listOf(Pair(3, bottomOffset),
                Pair(2, bottomOffset + 1), Pair(3, bottomOffset + 1), Pair(4, bottomOffset + 1),
                Pair(3, bottomOffset + 2))
            RockType.CORNER -> listOf(Pair(2, bottomOffset), Pair(3, bottomOffset), Pair(4, bottomOffset),
                Pair(4, bottomOffset + 1), Pair(4, bottomOffset + 2))
            RockType.V_LINE -> listOf(Pair(2, bottomOffset), Pair(2, bottomOffset + 1), Pair(2, bottomOffset + 2), Pair(2, bottomOffset + 3))
            RockType.SQUARE -> listOf(Pair(2, bottomOffset), Pair(3, bottomOffset),
                Pair(2, bottomOffset + 1), Pair(3, bottomOffset + 1))
        }
    }

    fun parseInput(input: String): List<Direction> {
        return input.toList().map { when(it){
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            else -> throw NotImplementedError("Invalid direction $it")
        }}
    }
}

enum class Direction {
    LEFT,
    RIGHT,
    DOWN
}

enum class RockType {
    H_LINE,
    PLUS,
    CORNER,
    V_LINE,
    SQUARE
}

class InfiniteGenerator<T>(private val order: List<T>) {
    var num = 0
    fun next(): T {
        return order[num % order.size].also { num++ }
    }
}
