package com.jacobhyphenated.advent2022.day19

import com.jacobhyphenated.advent2022.Day

/**
 * Day 19: Not Enough Minerals
 *
 * We want to collect geodes using a geode producing robot.
 * To build the robot we need other materials, and materials to build robots to harvest those materials.
 * It takes 1 minute for a robot to collect a material and 1 minute to build a robot.
 *
 * We have several blueprints that show different ways to manufacture robots (must use one blueprint at a time).
 * We start with 1 ore producing robot.
 */
class Day19: Day<List<Blueprint>> {
    override fun getInput(): List<Blueprint> {
        return parseInput(readInputFile("day19"))
    }

    /**
     * Find out how many geodes we can collect in 24 minutes.
     * The in quality level is the blueprint's id (index + 1) * the most geodes you can collect with that blueprint.
     * Return the sum of all quality levels for the list of blueprints
     */
    override fun part1(input: List<Blueprint>): Int {
        return input.mapIndexed { i, blueprint ->
            (i+1) * maximizeGeodes(Manufacture(blueprint), 24)
        }.sum()

    }

    /**
     * Now we have 32 minutes, but we only have the first 3 blueprints.
     * Multiply the total number of geodes we collect for each blueprint
     */
    override fun part2(input: List<Blueprint>): Int {
        val blueprints = input.take(3)
        return blueprints.fold(1) { acc, blueprint ->  acc * maximizeGeodes(Manufacture(blueprint), 32) }
    }

    /**
     * Recursive DFS and memoization
     *
     * @param manufacture Represents a comprehensive look at state, including blueprint, robot counts, and material counts
     * @param minute How many minutes are left
     * @param solutions a set of known solutions to the problem, used to prune DFS branches
     * @param duplicates a store of the state (manufacture + minute) that we have already solved.
     * Used to avoid visiting a DFS branch we have already visited
     */
    private fun maximizeGeodes(manufacture: Manufacture,
                               minute: Int,
                               solutions: MutableSet<Int> = mutableSetOf(0),
                               duplicates: MutableMap<Pair<ManufactureState, Int>, Int> = mutableMapOf()): Int {
        // Time's up. How many geodes did we collect?
        if (minute <= 0) {
            solutions.add( manufacture.materialCounts[Material.GEODE] ?: 0)
            return manufacture.materialCounts[Material.GEODE] ?: 0
        }

        // Have we seen this state before? If so, use memoization to skip repeating work
        val currentState = manufacture.exportState()
        if (Pair(currentState, minute) in duplicates) {
            return duplicates[Pair(currentState, minute)]!!
        }

        // Let's assume we can create a new Geode robot once per minute
        val n = minute - 1
        val currentGeodeProduction = minute * (manufacture.robotCounts[Material.GEODE] ?: 0)
        val currentGeodeCount = manufacture.materialCounts[Material.GEODE] ?: 0
        // use sequence 1+2+3+...+n == (n * (n+1)) / 2
        val theoreticalMax = ((n * (n+1)) / 2)  + currentGeodeProduction + currentGeodeCount
        // The theoretical max (best case geode production from current state) must be better than a known solution
        if (theoreticalMax < solutions.max()) {
            duplicates[Pair(currentState, minute)] = 0
            return 0
        }

        // If we can build a geode robot, do that - no need to explore other options
        if (manufacture.canBuild(Material.GEODE)) {
            val newOption = manufacture.copy()
            newOption.build(Material.GEODE)
            newOption.materialCounts[Material.GEODE] = (newOption.materialCounts[Material.GEODE] ?: 0) - 1
            newOption.collect()
            return maximizeGeodes(newOption, minute - 1, solutions, duplicates).also {
                duplicates[Pair(currentState, minute)] = it
            }
        }

        val paths = mutableListOf<Int>()
        for (material in Material.values()) {

            // we don't need to build another robot if the robot count >= the max cost needed to build something
            if ((manufacture.robotCounts[material] ?: 0) >= manufacture.maxMaterialNeeded.getValue(material)) {
                continue
            }

            if (manufacture.canBuild(material)) {
                val newOption = manufacture.copy()
                newOption.build(material)
                // don't count this new robot for this round
                newOption.materialCounts[material] = (newOption.materialCounts[material] ?: 0) - 1
                newOption.collect()
                paths.add(maximizeGeodes(newOption, minute - 1, solutions, duplicates))
            }
        }
        val skipBuild = manufacture.copy()
        skipBuild.collect()
        paths.add(maximizeGeodes(skipBuild, minute - 1, solutions, duplicates))
        return paths.max().also {
            duplicates[Pair(currentState, minute)] = it
        }
    }

    fun parseInput(input: String): List<Blueprint> {
        return input.lines().map { line ->
            val (_, blueprintLine) = line.split(": ")
            val costMap = blueprintLine.removeSuffix(".").split(". ").associate { robotLine ->
                val (materialPart, costPart) = robotLine.split(" robot costs ")
                val material = materialFromString(materialPart.split(" ").last())
                val costs = costPart.split(" and ").map { componentPart ->
                    val (cost, mat) = componentPart.split(" ").map { it.trim() }
                    Pair(materialFromString(mat), cost.toInt())
                }
                material to costs
            }
            Blueprint(costMap)
        }
    }

    private fun materialFromString(s: String): Material {
        return when(s) {
            "ore" -> Material.ORE
            "clay" -> Material.CLAY
            "obsidian" -> Material.OBSIDIAN
            "geode" -> Material.GEODE
            else -> throw NotImplementedError("Invalid material: $s")
        }
    }
}

class Blueprint(
    val robotCosts: Map<Material, List<Pair<Material, Int>>>
)

enum class Material {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}

class Manufacture(
    private val blueprint: Blueprint,
    var materialCounts: MutableMap<Material, Int> = mutableMapOf(),
    var robotCounts: MutableMap<Material, Int> = mutableMapOf(Material.ORE to 1)
){
    val maxMaterialNeeded: Map<Material, Int> = Material.values().associateWith { material ->
        blueprint.robotCosts.values
            .flatten()
            .filter { (mat, _) -> mat == material }
            .maxOfOrNull { (_, cost) -> cost } ?: Int.MAX_VALUE
    }

    fun copy(): Manufacture {
        return Manufacture(blueprint, materialCounts.copyToMutable(), robotCounts.copyToMutable() )
    }

    fun canBuild(material: Material): Boolean {
        return blueprint.robotCosts.getValue(material).all { (input, cost) -> (materialCounts[input] ?: 0) >= cost }
    }

    fun build(material: Material) {
        blueprint.robotCosts.getValue(material).forEach { (input, cost) ->
            materialCounts[input] = materialCounts.getValue(input) - cost
        }
        robotCounts[material] = (robotCounts[material] ?: 0) + 1
    }

    fun collect() {
        robotCounts.forEach{ (robot, count) ->
            materialCounts[robot] = (materialCounts[robot] ?: 0 ) + count
        }
    }

    fun exportState(): ManufactureState {
        val materials = Material.values().map { material -> materialCounts[material] ?: 0 }.toIntArray()
        val robots = Material.values().map { material -> robotCounts[material] ?: 0 }.toIntArray()
        return ManufactureState(materials, robots)
    }
}

/**
 * Optimize for storage and performance using primitive IntArrays
 * Override equals and hash code implementations for Array types as data classes prefer List<T>
 */
data class ManufactureState(val materials: IntArray, val robots: IntArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ManufactureState

        if (!materials.contentEquals(other.materials)) return false
        if (!robots.contentEquals(other.robots)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = materials.contentHashCode()
        result = 31 * result + robots.contentHashCode()
        return result
    }

}

fun <K,V> Map<K,V>.copyToMutable(): MutableMap<K,V> {
    return this.map { (key,value) -> key to value }.toMap().toMutableMap()
}