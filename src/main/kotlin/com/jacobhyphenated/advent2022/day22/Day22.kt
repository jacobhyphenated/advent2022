package com.jacobhyphenated.advent2022.day22

import com.jacobhyphenated.advent2022.Day

typealias Board = Map<Pair<Int,Int>, Tile>

/**
 * Day 22: Monkey Map
 *
 * You are given an irregularly shaped map in a 2d grid
 * The map contains '.' for empty space, and '#' for walls.
 * empty space means the map should wrap around.
 *
 * You also have instructions that involve moving forward and rotating the direction you are facing.
 */
class Day22: Day<Pair<Board, List<Instruction>>> {
    override fun getInput(): Pair<Board, List<Instruction>> {
        return parseInput(readInputFile("day22"))
    }

    /**
     * Follow the instructions. When the map wraps around,
     * stay in the same row or column you are moving through, but find
     * the first non-empty space of the opposite side.
     *
     * To determine the solution, you need the row and column (1 based)
     * and the facing (0: right, 1: down, 2: left, 3: up).
     * return the sum of 1000 times the row, 4 times the column, and the facing
     */
    override fun part1(input: Pair<Board, List<Instruction>>): Int {
        return followInstructions(input, false)
    }

    /**
     * The irregularly shaped map is actually a cube. You need to fold the different sides to make a cube.
     * Wrap arounds continue onto the adjacent cube face.
     * Note: this may change the facing direction in 2d space.
     */
    override fun part2(input: Pair<Board, List<Instruction>>): Int {
        return followInstructions(input, true)
    }

    /**
     * The instruction engine for parts 1 and 2 is the same except for how we calculate wrap around spaces.
     */
    private fun followInstructions(input: Pair<Board, List<Instruction>>, isCube: Boolean): Int {
        var inspect = Pair(0,0)
        val (b, instructions) = input
        val board = b.withDefault { Tile.EMPTY }
        while (board[inspect] != Tile.OPEN) {
            inspect = Pair(0, inspect.second + 1)
        }
        var currentLocation = inspect
        var facing = Facing.RIGHT

        val maxR = board.keys.maxOf { (r,_) -> r }
        val maxC = board.keys.maxOf { (_,c) -> c }

        instructions.forEach { instruction ->
            if (instruction.turn != null) {
                facing = facing.turn(instruction.turn)
            } else {
                var move = instruction.move!!
                while (move > 0) {
                    val (r,c) = currentLocation
                    val next = when(facing) {
                        Facing.UP -> Pair(r-1, c)
                        Facing.RIGHT -> Pair(r, c+1)
                        Facing.DOWN -> Pair(r+1, c)
                        Facing.LEFT -> Pair(r, c-1)
                    }
                    if (board[next] == Tile.OPEN) {
                        currentLocation = next
                    } else if (board[next] == Tile.WALL) {
                        // stop when we hit a wall
                        break
                    }
                    else { // empty space - we need to wrap around
                        val (oppositeEdge, direction) =  if (!isCube) {
                            findLinearWrapAround(currentLocation, facing, maxR, maxC, board)
                        } else {
                            findCubeWrapAround(currentLocation, facing, maxR, maxC)
                        }
                        if (board[oppositeEdge] == Tile.WALL) {
                            // If the wrap around space is a wall, don't wrap around, stop moving.
                            break
                        } else {
                            currentLocation = oppositeEdge
                            facing = direction
                        }
                    }
                    move--
                }
            }
        }

        return (currentLocation.first + 1) * 1000 + (currentLocation.second + 1) * 4 + facing.value
    }

    /**
     * To calculate linear wrap around spaces, go in the opposite direction until we find an empty tile.
     * That last valid space is our wrap around tile.
     */
    private fun findLinearWrapAround(current: Pair<Int,Int>, facing: Facing, maxR: Int, maxC: Int, board: Board): Pair<Pair<Int,Int>, Facing> {
        val (r,c) = current
        val wrapAroundSearch = when (facing) {
            Facing.RIGHT -> (c downTo  0).map { Pair(r,it) }
            Facing.DOWN -> (r downTo 0).map { Pair(it, c) }
            Facing.LEFT -> (c .. maxC).map { Pair(r, it) }
            Facing.UP -> (r .. maxR).map { Pair(it, c) }
        }
        val oppositeEdge = wrapAroundSearch.takeWhile { board.getValue(it) != Tile.EMPTY }.last()
        return Pair(oppositeEdge, facing)
    }

    /**
       Solve the cube wrap around specifically for this puzzle input by splitting into 6 quadrant sides of the cube:

       Folding the Cube Shaped like:
            21
            3
           54
           6

         1 bottom <-> 3 right
         1 top <-> 6 bottom
         1 right <-> 4 right
         2 top <-> 6 left
         2 left <-> 5 left
         3 left <-> 5 top
         4 bottom <-> 6 right

        Note: I cut out a piece of paper and folded it to figure out how these fit together
     */
    private fun findCubeWrapAround(current: Pair<Int,Int>, facing: Facing, maxR: Int, maxC: Int): Pair<Pair<Int,Int>, Facing> {
        val sideLength = 50 // hardcoded to puzzle input
        val (r,c) = current
        val quadrant = if (r < sideLength) {
            if (c < sideLength * 2) { 2 } else { 1 }
        }
        else if (r < sideLength * 2) { 3 }
        else if (r < sideLength * 3) {
            if (c < sideLength ) { 5 } else { 4 }
        }
        else { 6 }

        return when (quadrant) {
            1 -> when (facing) {
                Facing.UP -> Pair(Pair(maxR, c % sideLength), Facing.UP)
                Facing.RIGHT -> Pair(Pair(maxR - sideLength - r, c - sideLength), Facing.LEFT)
                Facing.DOWN -> Pair(Pair(c - sideLength, maxC - sideLength), Facing.LEFT)
                else -> throw IllegalStateException("Quadrant 1")
            }
            2 -> when (facing) {
                Facing.UP -> Pair(Pair(c + sideLength * 2,0), Facing.RIGHT)
                Facing.LEFT -> Pair(Pair(maxR - sideLength - r,0), Facing.RIGHT)
                else -> throw IllegalStateException("Quadrant 2")
            }
            3 -> when (facing) {
                Facing.RIGHT -> Pair(Pair(sideLength - 1, r + sideLength), Facing.UP)
                Facing.LEFT -> Pair(Pair(sideLength * 2, r - sideLength), Facing.DOWN)
                else -> throw IllegalStateException("Quadrant 3")
            }
            4 -> when (facing) {
                Facing.RIGHT -> Pair(Pair(maxR - sideLength - r, maxC), Facing.LEFT)
                Facing. DOWN -> Pair(Pair(c + sideLength * 2, sideLength - 1), Facing.LEFT)
                else -> throw IllegalStateException("Quadrant 4")
            }
            5 -> when (facing) {
                Facing.UP -> Pair(Pair(c + sideLength, sideLength), Facing.RIGHT)
                Facing.LEFT -> Pair(Pair(maxR - sideLength - r, sideLength), Facing.RIGHT)
                else -> throw IllegalStateException("Quadrant 5")
            }
            6 -> when (facing) {
                Facing.LEFT -> Pair(Pair(0, r - sideLength * 2), Facing.DOWN)
                Facing.RIGHT -> Pair(Pair(maxR - sideLength, r - sideLength * 2), Facing.UP)
                Facing.DOWN -> Pair(Pair(0, c + sideLength * 2), Facing.DOWN)
                else -> throw IllegalStateException("Quadrant 6")
            }
            else -> throw IllegalStateException("Invalid Quadrant $quadrant")
        }
    }

    fun parseInput(input: String): Pair<Board, List<Instruction>> {
        val (boardInput, instructionInput) = input.split("\n\n")

        val board = mutableMapOf<Pair<Int,Int>, Tile>()
        boardInput.lines().forEachIndexed { row, line ->
            line.toList().forEachIndexed { col, c ->
                val tile = when(c) {
                    ' ' -> Tile.EMPTY
                    '.' -> Tile.OPEN
                    '#' -> Tile.WALL
                    else -> throw NotImplementedError("Invalid board character $c")
                }
                board[Pair(row,col)] = tile
            }
        }

        val instructions = mutableListOf<Instruction>()
        var buffer = ""
        for (c in instructionInput) {
            if (c == 'R' || c == 'L') {
                if (buffer.isNotEmpty()) {
                    instructions.add(Instruction(move = buffer.toInt()))
                    buffer = ""
                }
                val turn = when (c) {
                    'R' -> Turn.RIGHT
                    'L' -> Turn.LEFT
                    else -> throw NotImplementedError("Invalid instruction character $c")
                }
                instructions.add(Instruction(turn = turn))
            }
            else {
                buffer += c
            }
        }
        if (buffer.isNotEmpty()) {
            instructions.add(Instruction(move = buffer.toInt()))
        }
        return Pair(board, instructions)
    }
}

enum class Facing(val value: Int) {
    RIGHT(0),
    DOWN(1),
    LEFT(2),
    UP(3);

    fun turn(turnDirection: Turn): Facing {
        val right = Turn.RIGHT == turnDirection
        return when (this) {
            RIGHT -> if (right) { DOWN } else { UP }
            DOWN -> if (right) { LEFT } else { RIGHT }
            LEFT ->  if (right) { UP } else { DOWN }
            UP -> if (right) { RIGHT } else { LEFT }
        }
    }
}

enum class Turn {
    RIGHT,
    LEFT
}

enum class Tile {
    EMPTY,
    OPEN,
    WALL
}

class Instruction(val move: Int? = null, val turn: Turn? = null)