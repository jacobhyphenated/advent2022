package com.jacobhyphenated.advent2022.day23

import com.jacobhyphenated.advent2022.Day

/**
 * Day 23: Unstable Diffusion
 *
 * The elves need to spread out to plant new trees.
 * The puzzle input is all the elves starting locations.
 * The elves look in a certain order of directions: (north,south,west,east) to determine if they should move or not.
 * The order changes each round.
 *
 * Example, an elf will propose a move north if the north, north-east, and north-west spaces are clear
 *
 * Once all the elves have proposed what space to move to, they only move to that space if no other elf has
 * also proposed moving to that space. Then the round is done.
 */
class Day23: Day<List<Pair<Int,Int>>> {
    override fun getInput(): List<Pair<Int, Int>> {
        return parseInput(readInputFile("day23"))
    }

    /**
     * Simulate 10 rounds of movement.
     * Then looking at the grid rectangle bounded by a space where an elf occupies the edge,
     * how many open spaces with no elf are there?
     */
    override fun part1(input: List<Pair<Int, Int>>): Any {
        var elfLocations = input.toSet()
        repeat(10){ roundNum ->
            elfLocations = doRound(elfLocations, roundNum)
        }

        val rowMin = elfLocations.minOf { (r, _) -> r }
        val rowMax = elfLocations.maxOf { (r, _) -> r }
        val colMin = elfLocations.minOf { (_, c) -> c }
        val colMax = elfLocations.maxOf { (_, c) -> c }

        return (rowMin .. rowMax).flatMap { row -> (colMin .. colMax).filter { col ->
            Pair(row,col) !in elfLocations
        } }.count()
    }

    /**
     * What is the first round where no elf moves?
     * Brute force approach solves it in around 1.5 seconds
     */
    override fun part2(input: List<Pair<Int, Int>>): Any {
        var elfLocations = input.toSet()
        var round = 0
        while (true) {
            val nextElfLocations = doRound(elfLocations, round)
            if (nextElfLocations == elfLocations) {
                break
            }
            elfLocations = nextElfLocations
            round++
        }
        return round + 1
    }

    /**
     * Simulate a round. Use the round number and some module math to determine the order for the directions.
     * @return the new list of elf locations at the end of the round
     */
    private fun doRound(elfLocations: Set<Pair<Int, Int>>, roundNum: Int): Set<Pair<Int,Int>> {
        // step 1 - figure out the proposed movement locations for all elves
        val proposed = mutableMapOf<Pair<Int,Int>, ProposedMovement>()
        elfLocations.forEach {
            val proposedLocation = proposeMovement(roundNum, it, elfLocations)
            val proposedMovement = proposed.getOrPut(proposedLocation) { ProposedMovement() }
            proposedMovement.originals.add(it)
        }

        // The elves only move to their proposed locations if no other elf also proposes the same location
        // otherwise those elves doen't mvoe at all
        return proposed.flatMap { (location, proposedMovement) ->
            if (proposedMovement.originals.size == 1) {
                listOf(location)
            } else {
                proposedMovement.originals
            }
        }.toSet()
    }

    /**
     * Determine what location this current elf should try to move to
     * @return the new proposed location, or the current location if the elf will not move this round
     */
    private fun proposeMovement(round: Int, current: Pair<Int,Int>, elfLocations: Set<Pair<Int,Int>>): Pair<Int,Int> {
        val directions = Direction.values()
        val startIndex = round % directions.size
        val (row, col) = current

        // If all 8 spaces surrounding the current space are free, the elf doesn't move
        val openAdjacent = (row-1 .. row+1).flatMap { r -> (col-1 .. col+1).filter { c -> Pair(r,c) !in elfLocations } }
        if (openAdjacent.size == 8) { return current }

        // Look in each direction in the proposed order to see what space to move to
        return directions.indices.firstNotNullOfOrNull { i ->
            val index = (i + startIndex) % directions.size
            when(directions[index]) {
                Direction.NORTH -> checkSpace(Pair(row - 1, col), (col - 1 .. col + 1).map { Pair(row-1, it) }, elfLocations)
                Direction.SOUTH -> checkSpace(Pair(row + 1, col), (col - 1 .. col + 1).map { Pair(row+1, it) }, elfLocations)
                Direction.WEST -> checkSpace(Pair(row, col - 1), (row - 1 .. row + 1).map { Pair(it, col-1) }, elfLocations)
                Direction.EAST -> checkSpace(Pair(row, col + 1), (row - 1 .. row + 1).map { Pair(it, col+1) }, elfLocations)
            }
        } ?: current
    }

    private fun checkSpace(toMove: Pair<Int,Int>, adjacent: List<Pair<Int,Int>>, elfLocations: Set<Pair<Int, Int>>): Pair<Int,Int>? {
        return if (adjacent.any { it in elfLocations }) { null } else { toMove }
    }

    fun parseInput(input: String): List<Pair<Int,Int>> {
        return input.lines().flatMapIndexed { row, line ->
            line.mapIndexed { col, character ->
                if (character == '#') { Pair(row, col) } else { null }
            }.filterNotNull()
        }
    }
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

class ProposedMovement(val originals: MutableList<Pair<Int,Int>> = mutableListOf())