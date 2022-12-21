package com.jacobhyphenated.advent2022.day21

import com.jacobhyphenated.advent2022.Day

/**
 * Day 21: Monkey Math
 *
 * Monkey math involves monkeys yelling out numbers.
 * Some always yell out a single number.
 * Others yell out the math result of two other monkey's numbers.
 *
 * The puzzle input shows each monkey's name, and the number or math operation associated with that monkey.
 */
class Day21: Day<List<Monkey>> {
    override fun getInput(): List<Monkey> {
        return parseInput(readInputFile("day21"))
    }

    /**
     * There is a monkey named root. What number does it yell out?
     */
    override fun part1(input: List<Monkey>): Long {
        val root = input.first { it.name == "root"}
        return root.getNumber()
    }

    /**
     * Root's operation is actually equals. The two monkey numbers in the operation should equal each other.
     * The monkey named "humn" is you. Ignore the number next to "humn".
     * What number should "humn" should out to make the root monkey's equivalence true?
     */
    override fun part2(input: List<Monkey>): Long {
        val root = input.first { it.name == "root"}
        return if (root.operation!!.monkey1.containsHuman()) {
            val equal = root.operation!!.monkey2.getNumber()
            root.operation!!.monkey1.determineHumanNumber(equal)
        } else {
            val equal = root.operation!!.monkey1.getNumber()
            root.operation!!.monkey2.determineHumanNumber(equal)
        }
    }

    fun parseInput(input: String): List<Monkey> {
        val monkeyMap = mutableMapOf<String, Monkey>()
        for (line in input.lines()) {
            val (name, value) = line.split(": ")
            val monkey = monkeyMap.getOrPut(name) { Monkey(name) }
            val operation = value.trim().split(" ")
            if (operation.size == 1) {
                monkey.number = operation[0].toLong()
            }
            else {
                val (m1, operator, m2) = operation
                val monkey1 = monkeyMap.getOrPut(m1) { Monkey(m1) }
                val monkey2 = monkeyMap.getOrPut(m2) { Monkey(m2) }
                monkey.operation = Operation(monkey1, monkey2, operator)
            }
        }
        return monkeyMap.values.toList()
    }
}

class Monkey (val name: String, var number: Long? = null, var operation: Operation? = null) {

    var resolveOperation: Long? = null

    // Recursive, since we need to know the other monkey's results first
    fun getNumber(): Long {
        if (number != null) {
            return number!!
        }
        // some lite memoization for performance
        if (resolveOperation != null) {
            return resolveOperation!!
        }
        return operation!!.invoke().also {
            resolveOperation = it
        }
    }

    fun containsHuman(): Boolean {
        if (name == "humn") { return true }
        if (number != null) { return false }
        return operation!!.monkey1.containsHuman() || operation!!.monkey2.containsHuman()
    }

    /**
     * Figure out what the human needs to shout by reversing the algebra
     * @param number equals side of the operation such that:
     * number = monkey1.getNumber() <operation> monkey2.getNumber()
     */
    fun determineHumanNumber(number: Long): Long {
        if (name == "humn") {
            return number
        }
        val op = operation!!

        // do some algebra. Pretty ugly
        return when (op.operator) {
            "*" -> {
                if (op.monkey1.containsHuman()) {
                    val otherNumber = op.monkey2.getNumber()
                    op.monkey1.determineHumanNumber(number / otherNumber)
                } else {
                    val otherNumber = op.monkey1.getNumber()
                    op.monkey2.determineHumanNumber(number / otherNumber)
                }
            }
            "/" -> {
                if (op.monkey1.containsHuman()) {
                    val otherNumber = op.monkey2.getNumber()
                    op.monkey1.determineHumanNumber(number * otherNumber)
                } else {
                    val otherNumber = op.monkey1.getNumber()
                    op.monkey2.determineHumanNumber(otherNumber / number)
                }
            }
            "+" -> {
                if (op.monkey1.containsHuman()) {
                    val otherNumber = op.monkey2.getNumber()
                    op.monkey1.determineHumanNumber(number - otherNumber)
                } else {
                    val otherNumber = op.monkey1.getNumber()
                    op.monkey2.determineHumanNumber(number - otherNumber)
                }
            }
            "-" -> {
                if (op.monkey1.containsHuman()) {
                    val otherNumber = op.monkey2.getNumber()
                    op.monkey1.determineHumanNumber(number + otherNumber)
                } else {
                    val otherNumber = op.monkey1.getNumber()
                    op.monkey2.determineHumanNumber((number - otherNumber) * -1)
                }
            }
            else -> throw NotImplementedError("Invalid operator ${op.operator}")
        }
    }
}

// Helper class to store the LHS and RHS sides of the operation and the mathmatical operator
class Operation(val monkey1: Monkey, val monkey2: Monkey, val operator: String) {

    fun invoke(): Long {
        val m1 = monkey1.getNumber()
        val m2 = monkey2.getNumber()
        return when(operator) {
            "+" -> m1 + m2
            "-" -> m1 - m2
            "*" -> m1 * m2
            "/" -> m1 / m2
            else -> throw NotImplementedError("Invalid Operation")
        }
    }
}