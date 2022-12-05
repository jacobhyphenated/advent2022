package com.jacobhyphenated.advent2022.day5

import com.jacobhyphenated.advent2022.Day

typealias Crates = List<MutableList<Char>>

class Day5: Day<Pair<Crates, List<Instruction>>> {
    override fun getInput(): Pair<Crates, List<Instruction>> {
        return parseInputString(readInputFile("day5"))
    }

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
    override fun part2(input: Pair<Crates, List<Instruction>>): String {
        val (crates, instructions) = input
        instructions.forEach { instruction ->
            val stack = crates[instruction.start].removeLastN(instruction.number)
            crates[instruction.destination].addAll(stack)
        }
        return crates.map { it.last() }.joinToString(separator = "")
    }

    private fun moveCrate(crates: Crates, from: Int, to: Int) {
        val crate = crates[from].removeLast()
        crates[to].add(crate)
    }

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

fun <T> MutableList<T>.removeLastN(n: Int): List<T> {
    val removedPart = this.slice(this.size - n until this.size)
    repeat(n) { this.removeLast() }
    return removedPart
}