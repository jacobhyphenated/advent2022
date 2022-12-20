package com.jacobhyphenated.advent2022.day18

import com.jacobhyphenated.advent2022.Day
import kotlin.math.absoluteValue

/**
 * Day 18: Boiling Boulders
 *
 * We can observe lava being shot into the air by the volcano.
 * Each unit of lava is represented by a 1x1x1 cube in 3d space by the puzzle input.
 * How the lava cools into stone is effected by how much surface area is exposed,
 * with adjacent cubes of lava sticking together.
 */
class Day18: Day<List<Cube>> {
    override fun getInput(): List<Cube> {
        return readInputFile("day18").lines().map { line ->
            val (x,y,z) = line.trim().split(",").map { it.toInt() }
            Cube(x, y, z)
        }
    }

    override fun warmup(input: List<Cube>): Any {
        return computeAdjacentCubes(input).size
    }

    /**
     * Count all the sides not connected to another cube. What is the surface area?
     */
    override fun part1(input: List<Cube>): Int {
        val cubes = computeAdjacentCubes(input)
        return cubes.sumOf { it.freeSides }
    }

    /**
     * Steam moves over the structure, but not diagonally.
     * Some internal sides of the lava cubes are never exposed to the water/steam.
     * What is only teh exterior surface of the lava cube structure?
     */
    override fun part2(input: List<Cube>): Int {
        // We use the value from part1 in this calculation
        val surfaceArea = part1(input)
        val allCubes = input.toSet()
        var interiorSpace = 0
        // find all cubes that are adjacent to a known lava cube
        // note: there will be duplicates. We want to count an open space multiple times,
        // based on the number of adjacent lava cubes to the open space
        val allSides = allCubes.flatMap {(x,y,z) ->
            listOf(
                Cube(x + 1, y, z),
                Cube(x - 1, y, z),
                Cube(x, y + 1, z),
                Cube(x, y - 1, z),
                Cube(x, y, z + 1),
                Cube(x, y, z - 1)
            )
        }
        for ((x,y,z) in allSides){
            if (Cube(x,y,z) in allCubes) {
                continue
            }
            // for each empty space cube, see if there is an unblocked path to the exterior
            // we know from the input that our structure is limited to [0,19] in all 3 axis
            val pathways = listOf((x downTo  0).map { Cube(it,y,z) },
                    (x + 1 .. 20).map { Cube(it, y, z) },
                    (y downTo 0).map { Cube(x, it, z) },
                    (y + 1 .. 20).map { Cube(x, it, z) },
                    (z downTo  0).map { Cube(x, y, it) },
                    (z + 1 .. 20).map { Cube(x, y, it) })
            if (pathways.all { path -> path.any { it in allCubes } }) {
                interiorSpace++
            }
        }
        // Return the total surface area subtracting each interior side that has no external exposure
        return surfaceArea - interiorSpace
    }

    /**
     * Cubes share a side if 2/3 coordinates are the same and the third is +- 1 apart.
     * Track what cubes are adjacent (share a side).
     */
    private fun computeAdjacentCubes(input: List<Cube>): List<Cube> {
        val cubes = input.map { it.copy() }
        for (cube in 0 until cubes.size -1) {
            for (other in cube + 1 until cubes.size) {
                val (x1, y1, z1) = cubes[cube]
                val (x2, y2, z2) = cubes[other]
                if ((x1 == x2 && y1 == y2 && (z1 - z2).absoluteValue == 1) ||
                    (y1 == y2 && z1 == z2 && (x1 - x2).absoluteValue == 1) ||
                    (x1 == x2 && z1 == z2 && (y1 - y2).absoluteValue == 1)
                ){
                    cubes[cube].adjacent.add(cubes[other])
                    cubes[other].adjacent.add(cubes[cube])
                }
            }
        }
        return cubes
    }
}

data class Cube(val x: Int, val y: Int, val z: Int) {
    val adjacent: MutableList<Cube> = mutableListOf()

    // The surface area for each cube is 6 - the number of adjacent cubes.
    val freeSides: Int
        get() = 6 - adjacent.size
}