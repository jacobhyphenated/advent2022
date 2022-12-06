package com.jacobhyphenated.advent2022.day5

import com.jacobhyphenated.advent2022.Day

// Distinguish between Crates as the data structure holding the initial state of the stacks
// and CratesMut which is a mutable data structure used when executing the crane instructions
typealias CratesMut = List<MutableList<Char>>
typealias Crates = List<List<Char>>

/**
 * Day 5: Supply Stacks
 *
 * A number of crates of supplies are placed in several stacks.
 * The initial positioning of the crates, and the instructions for the crane
 * are the puzzle input.
 */
class Day5: Day<Pair<Crates, List<Instruction>>> {
    override fun getInput(): Pair<Crates, List<Instruction>> {
        return parseInputString(readInputFile("day5"))
    }

    /**
     * The crane lifts one crate at a time.
     * An instruction of "move 3 from 1 to 2"
     * moves three crates from stack 1 to stack 2 moving one crate at a time.
     * The crane always removes from the top of the stack and places on the top of the stack
     *
     * Moving from left to right (lowest stack to highest stack) pull the character from the top
     * crate on the stack and return the string as a message.
     */
    override fun part1(input: Pair<Crates, List<Instruction>>): String {
        val (cratesInput, instructions) = input
        // don't mutate the input I want to re-use for part2 (I miss rust)
        val crates = cratesInput.map { it.toMutableList() }
        instructions.forEach { instruction ->
            repeat(instruction.number){
                moveCrate(crates, instruction.start, instruction.destination)
            }
        }
        return crates.map { it.last() }.joinToString(separator = "")
    }

    /**
     * The crane actually picks up all crates it needs to move in a single instruction at once.
     * So instead of moving crates in reverse order (part 1), it moves them in the same order.
     *
     * Return the message spelled by the top crate in each stack.
     */
    override fun part2(input: Pair<Crates, List<Instruction>>): String {
        val (cratesInput, instructions) = input
        val crates = cratesInput.map { it.toMutableList() }
        instructions.forEach { instruction ->
            val stack = crates[instruction.start].removeLastN(instruction.number)
            crates[instruction.destination].addAll(stack)
        }
        return crates.map { it.last() }.joinToString(separator = "")
    }

    private fun moveCrate(crates: CratesMut, from: Int, to: Int) {
        val crate = crates[from].removeLast()
        crates[to].add(crate)
    }

    /**
     * Parsing the input by splitting into two sections: crates and instructions
     *
     * For crates, use a bottom up approach
     * - find index of the character for each line that corresponds to a stack
     * - add crates to the stack in reverse order (bottom of stack first)
     *
     * Instructions are parsed out of the string
     * - subtract one from the "to" and "from" stacks to convert stack number to array index
     */
    fun parseInputString(input: String): Pair<Crates, List<Instruction>> {
        val (crateString, instructionString) = input.split("\n\n")
        val crateIndexMap: MutableMap<Int, MutableList<Char>> = mutableMapOf()
        val crateInputLines = crateString.lines().reversed()
        crateInputLines[0].forEachIndexed{ index, c ->
            if (!c.isWhitespace()){
                crateIndexMap[index] = mutableListOf()
            }
        }
        crateInputLines.slice(1 until crateInputLines.size).forEach { crateLine ->
            crateIndexMap.keys.forEach { index ->
                if (crateLine.length > index && !crateLine[index].isWhitespace()) {
                    // !! is safe because index must be a key for crateIndexMap. Wish the compiler could know that
                    crateIndexMap[index]!!.add(crateLine[index])
                }
            }
        }
        val crates = crateIndexMap.keys.sorted().map { crateIndexMap.getValue(it) }
        val instructions = instructionString.lines().map {
            val (move, toFrom) = it.split(" from ")
            val number = move.trim().split(" ").last().toInt()
            val (start, destination) = toFrom.split("to").map { c-> c.trim().toInt() }
            Instruction(start - 1, destination - 1, number)
        }
        return Pair(crates, instructions)
    }
}

data class Instruction(
    val start: Int,
    val destination: Int,
    val number: Int
)

/**
 * Remove the last N elements from a mutable list and return them.
 *
 * @param n the number of elements to be removed from the end of the list
 * @return A list containing the removed elements with order preserved
 */
fun <T> MutableList<T>.removeLastN(n: Int): List<T> {
    val removedPart = this.slice(this.size - n until this.size)
    repeat(n) { this.removeLast() }
    return removedPart
}