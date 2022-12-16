package com.jacobhyphenated.advent2022.day16

import com.jacobhyphenated.advent2022.Day
import kotlin.math.max

class Day16: Day<List<String>> {
    override fun getInput(): List<String> {
        return readInputFile("day16").lines()
    }

    override fun part1(input: List<String>): Int {
        val (start, valves) = createGraph(input)
        return maximizePressure(start, null, valves, 0, 30, setOf(), mutableSetOf(500))
    }

    override fun part2(input: List<String>): Int {
        val (start, valves) = createGraph(input)
        return maximizePressureWithElephant(start, null, start, null, valves, 0, 26, setOf(), mutableSetOf(500))
    }

    /**
     * Recursive function to do a DFS search of all possible paths to open the valves
     * @param currentValve The current location
     * @param previousValve the previous location - used to avoid backtracking for smarter paths
     * @param allValves A list of all valves in the graph
     * @param currentPressure how much pressure will be released in total based on the valves already opened
     * @param minute How much time is remaining
     * @param open a set showing which valves have been opened so far
     * @param solutions valid solutions for the amount of pressure released. Used to optimize and prune inefficient paths
     * @return The highest possible value for pressure to be released in the current DFS path
     */
    private fun maximizePressure(currentValve: Valve,
                                 previousValve: Valve?,
                                 allValves: List<Valve>,
                                 currentPressure: Int,
                                 minute: Int,
                                 open: Set<String>,
                                 solutions: MutableSet<Int>): Int {
        if (minute <= 0) {
            solutions.add(currentPressure)
            return currentPressure
        }
        if (allValves.all { it.pressure == 0 || it.name in open }) {
            solutions.add(currentPressure)
            return currentPressure
        }

        val potentialMax = currentPressure + allValves
            .filter { it.pressure > 0 && it.name !in open }
            .sumOf { it.pressure * (minute - 1) }
        if (potentialMax < (solutions.maxOrNull() ?: 0)) {
            return 0
        }

        val adjacent = currentValve.adjacent
            .filter { it.name != previousValve?.name }
            .let { it.ifEmpty { listOf(previousValve!!) } } // only visit the previous value if there are no other paths

        var bestPath = 0
        if (currentValve.pressure > 0 && currentValve.name !in open) {
            val nextOpen = open.toMutableSet()
            nextOpen.add(currentValve.name)
            val nextMinute = minute - 1
            val newPressure = currentPressure + currentValve.pressure * nextMinute
            bestPath = adjacent.maxOf { maximizePressure(it, currentValve, allValves, newPressure, nextMinute - 1, nextOpen, solutions) }
        }
        val passThrough = adjacent.maxOf { maximizePressure(it, currentValve, allValves, currentPressure, minute - 1, open, solutions) }
        return max(bestPath, passThrough)
    }

    private fun maximizePressureWithElephant(myCurrentValve: Valve,
                                             myPreviousValve: Valve?,
                                             elephantCurrentValve: Valve,
                                             elephantPreviousValve: Valve?,
                                             allValves: List<Valve>,
                                             currentPressure: Int,
                                             minute: Int,
                                             open: Set<String>,
                                             solutions: MutableSet<Int>): Int {
        if (minute <= 0) {
            solutions.add(currentPressure)
            return currentPressure
        }
        if (allValves.all { it.pressure == 0 || it.name in open }) {
            solutions.add(currentPressure)
            return currentPressure
        }

        val potentialMax = currentPressure + allValves
            .filter { it.pressure > 0 && it.name !in open }
            .sumOf { it.pressure * (minute - 1) }
        if (potentialMax < (solutions.maxOrNull() ?: 0)) {
            return 0
        }

        val myAdjacent = myCurrentValve.adjacent
            .filter { it.name != myPreviousValve?.name }
            .let { it.ifEmpty { listOf(myPreviousValve!!) } }
        val elephantAdjacent = elephantCurrentValve.adjacent
            .filter { it.name != elephantPreviousValve?.name }
            .let { it.ifEmpty { listOf(elephantPreviousValve!!) } }

        var bestPath = 0
        val openMyValve = myCurrentValve.pressure > 0 && myCurrentValve.name !in open
        val openElephantValve = elephantCurrentValve.name != myCurrentValve.name && elephantCurrentValve.pressure > 0 && elephantCurrentValve.name !in open

        if (openMyValve) {
            val nextOpen = open.toMutableSet()
            nextOpen.add(myCurrentValve.name)
            val newPressure = currentPressure + myCurrentValve.pressure * (minute - 1)
            bestPath = max(bestPath, elephantAdjacent.maxOf {
                maximizePressureWithElephant(myCurrentValve, myPreviousValve, it, elephantCurrentValve, allValves, newPressure, minute - 1, nextOpen, solutions)
            })
        }

        if (openElephantValve) {
            val nextOpen = open.toMutableSet()
            nextOpen.add(elephantCurrentValve.name)
            val newPressure = currentPressure + elephantCurrentValve.pressure * (minute - 1)
            bestPath = max(bestPath, myAdjacent.maxOf {
                maximizePressureWithElephant(it, myCurrentValve, elephantCurrentValve, elephantPreviousValve, allValves, newPressure, minute - 1, nextOpen, solutions)
            })
        }

        if (openElephantValve && openMyValve) {
            val nextOpen = open.toMutableSet()
            nextOpen.add(elephantCurrentValve.name)
            nextOpen.add(myCurrentValve.name)
            val newPressure = currentPressure + elephantCurrentValve.pressure * (minute - 1) + myCurrentValve.pressure * (minute - 1)
            bestPath = max(bestPath, maximizePressureWithElephant(myCurrentValve, myPreviousValve, elephantCurrentValve, elephantPreviousValve, allValves, newPressure, minute - 1, nextOpen, solutions))
        }
        return max(bestPath, myAdjacent.maxOf { my ->
            elephantAdjacent.maxOf { elph ->
                maximizePressureWithElephant(my, myCurrentValve, elph, elephantCurrentValve, allValves, currentPressure, minute - 1, open, solutions)
            }
        })
    }

    private fun createGraph(input: List<String>): Pair<Valve, List<Valve>> {
        val valves = mutableMapOf<String, Valve>()
        input.forEach { line ->
            val (valvePart, neighborPart) = line.split("; ")
            val (namePart, flowRate) = valvePart.split(" has flow rate=")
            val valve = Valve(namePart.split(" ")[1].trim(), flowRate.trim().toInt())
            valves[valve.name] = valve

            val knownNeighbors = neighborPart.removePrefix("tunnels lead to valves ")
                .removePrefix("tunnel leads to valve ") // This was seriously mean. Not cool AoC.
                .trim().split(", ")
                .mapNotNull { valves[it] }
            valve.adjacent.addAll(knownNeighbors)
            knownNeighbors.forEach { it.adjacent.add(valve) }
        }
        return Pair(valves.getValue("AA"), valves.values.toList())
    }
}

class Valve(
    val name: String,
    val pressure: Int,
    val adjacent: MutableList<Valve> = mutableListOf()
)