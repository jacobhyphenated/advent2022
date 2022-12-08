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

        // start at the top of the grid and look down
        for (row in 1 until input.size - 1) {
            var tallestFromTopDown = input[row][0]
            for (col in 1 until input[row].size - 1) {
                if (input[row][col] > tallestFromTopDown) {
                    visible.add(Pair(row,col))
                    tallestFromTopDown = input[row][col]
                }
                if (tallestFromTopDown == 9) {
                    break
                }
            }
        }

        // start at the bottom of the grid and look up
        for (row in 1 until input.size - 1) {
            var tallestFromBottomUp = input[row][input[row].size - 1]
            for (col in input[row].size - 2 downTo 1) {
                if (input[row][col] > tallestFromBottomUp) {
                    visible.add(Pair(row,col))
                    tallestFromBottomUp = input[row][col]
                }
                if (tallestFromBottomUp == 9) {
                    break
                }
            }
        }

        // Start on the left side of the grid and look right
        for (col in 1 until input[0].size - 1) {
            var tallestFromLeftToRight = input[0][col]
            for (row in 1 until input.size - 1) {
                if (input[row][col] > tallestFromLeftToRight) {
                    visible.add(Pair(row,col))
                    tallestFromLeftToRight = input[row][col]
                }
                if (tallestFromLeftToRight == 9) {
                    break
                }
            }
        }

        // start on the right side of the grid and look left
        for (col in 1 until input[0].size - 1) {
            var tallestFromRightToLeft = input[input.size - 1][col]
            for (row in input.size - 2 downTo 1) {
                if (input[row][col] > tallestFromRightToLeft) {
                    visible.add(Pair(row,col))
                    tallestFromRightToLeft = input[row][col]
                }
                if (tallestFromRightToLeft == 9) {
                    break
                }
            }
        }

        // return the interior visible trees + a count of all the edge trees
        return visible.size + (input.size - 1) * 4
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
        for (row in input.indices) {
            for (col in input[row].indices) {
                val treeHeight = input[row][col]

                var upScore = 0
                for (r in row - 1  downTo  0) {
                    upScore++
                    if (input[r][col] >= treeHeight) {
                        break
                    }
                }

                var downScore = 0
                for (r in row + 1 until input.size) {
                    downScore++
                    if (input[r][col] >= treeHeight) {
                        break
                    }
                }

                var leftScore = 0
                for (c in col - 1 downTo  0) {
                    leftScore++
                    if (input[row][c] >= treeHeight) {
                        break
                    }
                }

                var rightScore = 0
                for (c in col + 1 until input[row].size) {
                    rightScore++
                    if (input[row][c] >= treeHeight) {
                        break
                    }
                }

                val totalScore = upScore * downScore * leftScore * rightScore
                if (totalScore > largestScore){
                    largestScore = totalScore
                }
            }
        }
        return largestScore
    }
}