package com.jacobhyphenated.advent2022.day15

import com.jacobhyphenated.advent2022.Day
import kotlin.math.absoluteValue

/**
 * Day 15: Beacon Exclusion Zone
 *
 * There are a number of beacons. You have sensors that can show the beacon locations.
 * Each sensor knows its own location as well as the closest beacon to itself.
 * Distances are measured using Manhattan (taxicab) distance.
 *
 * There may exist beacons that are not picked up by the sensors as each sensor only shows the closest beacon.
 */
class Day15: Day<List<Sensor>> {
    override fun getInput(): List<Sensor> {
        return parseInput(readInputFile("day15"))
    }

    /**
     * In the 2d grid, look at only the y axis where y == 2,000,000
     * How many positions on this axis CANNOT contain a beacon?
     *
     * Do not count places that have beacons already.
     */
    override fun part1(input: List<Sensor>): Int {
        return countPositionsWithoutBeacon(input,2000000)
    }

    /**
     * There is a missing distress beacon in between 0 and 4,000,000 (both x and y).
     * Find the one location in that search space that is not excluded by the other sensor,
     * and therefore must contain the missing beacon.
     * Return the x coordinate * 4,000,000 + y coordinate
     */
    override fun part2(input: List<Sensor>): Long {
        return findBeaconFrequencyInRange(4000000, input)
    }

    /**
     * This is a brute force approach. Use a reasonable x range that might be in
     * the coverage exclusion area for each sensor. Evaluate every coordinate along that x range.
     * If it's in the coverage exclusion are for any sensor, a beacon cannot exist at that coordinate.
     *
     * Note: I might be able to get the runtime down by using a skip ahead approach like in part 2.
     */
    fun countPositionsWithoutBeacon(sensors: List<Sensor>, y: Int): Int {
        val maxDistance = sensors.maxOf { it.beaconDistance }
        val allXPositions = sensors.flatMap { listOf(it.x, it.closestBeacon.first) }
        val minX = allXPositions.min() - maxDistance
        val maxX = allXPositions.max() + maxDistance

        return (minX .. maxX).map { Pair(it, y) }.count { coordinate ->
            sensors.any { it.isInCoverageArea(coordinate) }
        }
    }

    /**
     * It's not possible to brute force a search space of 4,000,000 x 4,000,000
     *
     * We do need to search every y coordinate.
     * But we can skip ahead through chunks of each x coordinate.
     *
     * Use the manhattan distance from the search location to a nearby sensor.
     * Calculate the sensor's exclusion coverage area along the given x-axis and
     * skip the areas we know must be excluded by that sensor
     */
    fun findBeaconFrequencyInRange(maxRange: Int, sensors: List<Sensor>): Long {
        var x = 0
        var y = 0
        while (true) {
            if (y > maxRange) { break }
            if (x > maxRange) {
                y++
                x = 0
                continue
            }
            val current = Pair(x,y)
            // find the first sensor that this point is covered by
            // if we can't find a sensor that covers this point, this point must be the missing beacon
            val inRangeOf = sensors.firstOrNull { it.closestBeacon == current || it.isInCoverageArea(current) }
                ?: return current.first.toLong() * 4000000L + current.second

            // manhattan distance can be used to determine where we are within the exclusion coverage area
            val diff = inRangeOf.beaconDistance - calcManhattanDistance(Pair(inRangeOf.x, inRangeOf.y), current)
            val xDiff = if (current.first < inRangeOf.x) { (inRangeOf.x - current.first) * 2 + diff } else { diff }

            // if we're on the edge of the coverage area, xDiff will be 0, so always advance x by at least 1
            x += xDiff.coerceAtLeast(1)
        }
        return -1
    }

    fun parseInput(input: String): List<Sensor> {
        return input.lines().map { line ->
            val (sensorPart, beaconPart) = line.split(": closest beacon is at ")
            val (x,y) = sensorPart.split(",")
                .map { it.split("=")[1].trim().toInt() }
            val (beaconX, beaconY) = beaconPart.split(",")
                .map { it.split("=")[1].trim().toInt() }
            Sensor(x,y,Pair(beaconX, beaconY))
        }
    }
}

class Sensor(
    val x: Int,
    val y: Int,
    val closestBeacon: Pair<Int,Int>
) {
    val beaconDistance = calcManhattanDistance(Pair(x,y), closestBeacon)

    fun isInCoverageArea(point: Pair<Int,Int>): Boolean {
        if (point == closestBeacon) { return false }
        return calcManhattanDistance(Pair(x,y), point) <= beaconDistance
    }
}
private fun calcManhattanDistance(p1: Pair<Int,Int>, p2: Pair<Int,Int>): Int {
    val (x1,y1) = p1
    val (x2, y2) = p2
    return (x1 - x2).absoluteValue + (y1 - y2).absoluteValue
}