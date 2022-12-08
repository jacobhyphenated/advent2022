package com.jacobhyphenated.advent2022.day8

import com.jacobhyphenated.advent2022.Day

/**
 * Day 8: Treetop Tree House
 *
 * A 2d array is a grid of tree sizes (where 9 is the tallest possible tree)
 */
class Day8: Day<List<List<Int>>> {
    override fun getInput(): List<List<Int>> {
        return readInputFile("day8").lines()
            .map { line -> line.toCharArray().map { it.digitToInt() } }
    }

    /**
     * Find how many trees are visible from the edge of the grid.
     * All edge trees are visible
     * A tree is visible it is taller than the trees in front of it.
     *
     * Look down each row and column (left to right, right to left, top to bottom, and bottom to top)
     * Proceed in a line from the starting point counting all visible trees
     * This approach is more efficient than checking if each individual tree is visible at an edge
     */
    override fun part1(input: List<List<Int>>): Any {
        // use a set to store trees we can see to avoid duplicate counts
        val visible = mutableSetOf<Pair<Int,Int>>()

        // Combine ranges for each side. This results in multiple List<List<Pair<Int,Int>>>
        // where the Pair is a tuple of the row and column indexes
        val indices = input.indices
        val topToBottom = indices.map { r -> indices.map { c -> Pair(r,c) } }
        val bottomToTop = indices.map { r -> indices.reversed().map { c -> Pair(r,c) } }
        val leftToRight = indices.map { c -> indices.map { r -> Pair(r,c) } }
        val rightToLeft = indices.map { c -> indices.reversed().map { r -> Pair(r,c) } }

        val allSides = topToBottom + bottomToTop + leftToRight + rightToLeft

        // Each list of (Row,Col) that looks down one row or column is a "lineOfSight"
        // iterate down the line of sight and mark the trees that are visible
        val valueAtLocation = { pair: Pair<Int,Int> -> input[pair.first][pair.second] }
        for (lineOfSight in allSides) {
            var tallestSoFar = -1
            for (tree in lineOfSight) {
                if (valueAtLocation(tree) > tallestSoFar) {
                    visible.add(tree)
                    tallestSoFar = valueAtLocation(tree)
                }
                if (tallestSoFar == 9) {
                    break
                }
            }
        }

        return visible.size
    }

    /**
     * Find which tree has the best view.
     * The view is defined by how many other trees you can see from each edge of the tree.
     * Your view is blocked once you reach a tree of equal or greater height than the starting tree.
     * Multiply the view counts from each edge to get the view score.
     *
     * return the best view score possible in the grid of trees.
     */
    override fun part2(input: List<List<Int>>): Any {
        var largestScore = 0
        // Look from the perspective of each tree in the grid
        for (row in input.indices) {
            for (col in input[row].indices) {
                val treeHeight = input[row][col]

                // Sizes of trees in each direction, in order
                val up = (row - 1 downTo 0).map { input[it][col] }
                val down = (row + 1 until input.size).map { input[it][col] }
                val left = (col - 1 downTo 0).map { input[row][it] }
                val right = (col + 1 until input.size).map { input[row][it] }

                // calculate the number of trees that can be seen in each direction
                val viewScore = { lineOfSight: List<Int> ->
                    var score = 0
                    for (tree in lineOfSight) {
                        score++
                        if (tree >= treeHeight) {
                            break
                        }
                    }
                    score
                }

                val totalScore = viewScore(up) * viewScore(down) * viewScore(left) * viewScore(right)
                if (totalScore > largestScore){
                    largestScore = totalScore
                }
            }
        }
        return largestScore
    }
}