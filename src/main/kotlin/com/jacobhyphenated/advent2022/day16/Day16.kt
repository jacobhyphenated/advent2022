package com.jacobhyphenated.advent2022.day16

import com.jacobhyphenated.advent2022.Day
import java.util.PriorityQueue

/**
 * Day 16: Proboscidea Volcanium
 *
 * The cave system contains several different valves connected by tunnels.
 * Each valve releases some amount of pressure per minute once it's been opened.
 *
 * It takes 1 minute to open a valve and 1 minute to move to an adjacent valve location.
 */
class Day16: Day<List<String>> {
    override fun getInput(): List<String> {
        return readInputFile("day16").lines()
    }

    /**
     * You have 30 minutes. Maximize how much pressure can be released in that time.
     * return the total pressure released
     */
    override fun part1(input: List<String>): Int {
        val (start, valves) = createGraph(input)
        val closedValves = valves.filter { it.pressure > 0 }
        val travelTimes = (closedValves + start).associate { it.name to findPathsToValves(it) }
        val startingPositions = listOf(Pair(start.name, 30))
        return maximizePressure(startingPositions, 0, closedValves.map { it.name }.toSet(), travelTimes, valves.associateBy { it.name }, mutableSetOf(1000))
    }

    /**
     * You spend 4 minutes training an elephant to help you. You and the elephant move independently.
     * return the maximum total pressure that can be released in the remaining 26 minutes.
     */
    override fun part2(input: List<String>): Int {
        val (start, valves) = createGraph(input)
        val closedValves = valves.filter { it.pressure > 0 }
        val travelTimes = (closedValves + start).associate { it.name to findPathsToValves(it) }
        val startingPositions = listOf(Pair(start.name, 26), Pair(start.name, 26))
        return maximizePressure(startingPositions, 0, closedValves.map { it.name }.toSet(), travelTimes, valves.associateBy { it.name }, mutableSetOf(1000))
    }

    /**
     * Many of the valves have 0 pressure and function as pass-through locations.
     * Set our destinations to only the valves with pressure that we haven't already opened.
     * Use a DFS to recursively find the path that maximizes the total pressure released
     *
     * @param destinationStack A list of destination locations (1 for part 1, 2 for part2 and the elephant).
     * The destination has the valve name and the time of arrival at that location.
     * @param currentPressure how much pressure will be released based on the valves already opened
     * @param closedValves the remaining closed valves that need to be opened.
     * @param travelTimes a map with pre-calculated travel times to each node from each node
     * @param allValves Every valve as a map to easily look up a valve by name
     * @param solutions valid solutions for the amount of pressure released. Used to optimize and prune inefficient paths
     */
    private fun maximizePressure(destinationStack: List<Pair<String, Int>>,
                                 currentPressure: Int,
                                 closedValves: Set<String>,
                                 travelTimes: Map<String, Map<String, Int>>,
                                 allValves: Map<String, Valve>,
                                 solutions: MutableSet<Int>): Int {

        if (destinationStack.isEmpty()) {
            solutions.add(currentPressure)
            return currentPressure
        }

        // Pull off the first destination we'll get to
        val current = destinationStack.maxBy { (_, minute) -> minute }
        val (valveName, minute) = current
        val valve = allValves.getValue(valveName)
        var newPressure = currentPressure
        val updatedClosedValves = closedValves.toMutableSet()
        var updatedMinute = minute
        // If the valve at this destination is closed, open it.
        if (valveName in closedValves) {
            updatedClosedValves.remove(valveName)
            updatedMinute -= 1
            newPressure += updatedMinute * valve.pressure
        }

        // If we were to magically turn all the valves on,
        // would that added pressure be better than another solved DFS path?
        val potentialMax = newPressure + allValves.values
            .filter { it.name in updatedClosedValves }
            .sumOf { it.pressure * updatedMinute }
        if (potentialMax < (solutions.maxOrNull() ?: 0)) {
            return 0
        }

        if (updatedClosedValves.isEmpty()) {
            solutions.add(newPressure)
            return newPressure
        }

        // Recursive DFS to all remaining closed valves
        val travelTimeForCurrent = travelTimes.getValue(valveName)
        val remainingDestinations = destinationStack.toMutableList().apply { remove(current) }
        return updatedClosedValves
            .map { Pair(it, travelTimeForCurrent.getValue(it)) }
            .filter { (node, _) -> node !in remainingDestinations.map { it.first } }
            .map { (node, travelTime) ->
                remainingDestinations.toMutableList()
                    .apply { add(Pair(node, updatedMinute - travelTime)) }
                    .filter { (_, time) -> time >= 0 } // We need to get to the location before time runs out
            }
            .maxOfOrNull { maximizePressure(it, newPressure, updatedClosedValves, travelTimes, allValves, solutions) }
            // if there are no new valid destinations, finish off the paths in the stack
            ?: maximizePressure(remainingDestinations, newPressure, updatedClosedValves, travelTimes, allValves, solutions)
    }


    private fun findPathsToValves(startNode: Valve): Map<String, Int> {
        val distances = mutableMapOf(startNode.name to 0)
        val queue = PriorityQueue<PathCost> { a, b -> a.cost - b.cost }
        queue.add(PathCost(startNode, 0))
        var current: PathCost

        do {
            current = queue.remove()
            // If we already found a less expensive way to reach this position
            if (current.cost > (distances[current.valve.name] ?: Int.MAX_VALUE)) {
                continue
            }

            current.valve.adjacent.forEach {
                // cost is the number of steps taken, increases by 1 for each move
                val cost = distances.getValue(current.valve.name) + 1
                // If the cost to this space is less than what was previously known, put this on the queue
                if (cost < (distances[it.name] ?: Int.MAX_VALUE)) {
                    distances[it.name] = cost
                    queue.add(PathCost(it, cost))
                }
            }
        } while (queue.size > 0)
        return distances
    }

    private fun createGraph(input: List<String>): Pair<Valve, List<Valve>> {
        val valves = mutableMapOf<String, Valve>()
        input.forEach { line ->
            val (valvePart, neighborPart) = line.split("; ")
            val (namePart, flowRate) = valvePart.split(" has flow rate=")
            val valve = Valve(namePart.split(" ")[1].trim(), flowRate.trim().toInt())
            valves[valve.name] = valve

            val knownNeighbors = neighborPart.removePrefix("tunnels lead to valves ")
                .removePrefix("tunnel leads to valve ") // Ugh. Not cool AoC.
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

class PathCost(val valve: Valve, val cost: Int)