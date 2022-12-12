package com.jacobhyphenated.advent2022.day11

import com.jacobhyphenated.advent2022.Day

/**
 * Day 11: Monkey in the Middle
 *
 * Monkeys are playing keep away with your items. Each item has a score of how worried you are about losing it.
 * During a round, each monkey inspects each item it has, calculates a new worry score (operation)
 * Then does a "test" to determine which other monkey to throw the item to.
 */
class Day11: Day<List<Monkey>> {
    override fun getInput(): List<Monkey> {
        return parseInput(readInputFile("day11"))
    }

    /**
     * Every time a monkey inspects your item, you divide the worry score by 3 out of relief.
     * Calculate how many times each monkey inspects an item.
     * After 20 rounds, return the product of the two highest inspection counts.
     */
    override fun part1(input: List<Monkey>): Long {
        // deep copy for mutability
        val monkeys = input.map { it.copy(items = it.items.toMutableList()) }

        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.doRound().forEach { (item, monkeyIndex) ->
                    monkeys[monkeyIndex].items.add(item)
                }
            }
        }
        return monkeys.map { it.itemsInspected }
            .sorted().reversed()
            .take(2)
            .reduce { a, b -> a * b}
    }

    /**
     * Your worry score no longer gets divided by 3 after the inspection. Also, go for 10,000 rounds.
     * return the product of the two highest inspection counts.
     */
    override fun part2(input: List<Monkey>): Long {
        // deep copy for mutability
        val monkeys = input.map { it.copy(items = it.items.toMutableList()) }

        // Even longs will eventually overflow over 10,000 rounds.
        // However, because every test uses a modulo of a prime number, we can get use the least common multiple
        val leastCommonMultiple = monkeys.map { it.divisibleTest }.reduce{ a, b -> a*b}

        repeat(10_000) {
            monkeys.forEach { monkey ->
                monkey.doRound(true).forEach { (item, monkeyIndex) ->
                    monkeys[monkeyIndex].items.add(item)
                }
            }
            // Using the least common multiple, we can mod the worry scores to keep them well below overflow
            monkeys.forEach { monkey -> monkey.reduceItems(leastCommonMultiple) }
        }
        return monkeys.map { it.itemsInspected }
            .sorted().reversed()
            .take(2)
            .reduce { a, b -> a * b}
    }

    fun parseInput(input: String): List<Monkey> {
        return input.split("\n\n").map {
            val lines = it.lines()
            val startingItems = lines[1].trim().split(":")[1].trim().split(", ").map { s -> s.toLong() }
            val operationString = lines[2].split("=")[1].trim()
            val divisibleTest = lines[3].trim().split(" ").last().toLong()
            val ifTestTrue = lines[4].trim().split(" ").last().toInt()
            val ifTestFalse = lines[5].trim().split(" ").last().toInt()

            val (lhs, operator, rhs) = operationString.split(" ")
            val resolveValue = { value: String, oldValue: Long -> if (value == "old") { oldValue } else { value.toLong() } }
            val operation = when(operator) {
                "+" -> { oldValue: Long -> resolveValue(lhs, oldValue) + resolveValue(rhs, oldValue) }
                "*" ->  { oldValue: Long -> resolveValue(lhs, oldValue) * resolveValue(rhs, oldValue) }
                else -> throw NotImplementedError("Invalid operator $operator")
            }
            Monkey(items = startingItems.toMutableList(), operation, divisibleTest, ifTestTrue, ifTestFalse)
        }
    }
}

data class Monkey(
    val items: MutableList<Long>,
    private val operation: (Long) -> Long,
    val divisibleTest: Long,
    private val ifTestTrue: Int,
    private val ifTestFalse: Int
) {
    var itemsInspected = 0L

    /**
     * Perform the round of inspections for this monkey.
     * The list of items will be empty after this function completes
     *
     * @param extraWorry do not divide the worry score by 3 if this is true. Used for part 2.
     * @return A List of  pairs containing the item score and the index of the monkey it should be passed to
     */
    fun doRound(extraWorry: Boolean = false): List<Pair<Long,Int>> {
        return items
            .map { operation(it) }
            .map { if (extraWorry) { it } else { it / 3 } }
            .map {
                itemsInspected++
                if (it % divisibleTest == 0L) {
                    Pair(it, ifTestTrue)
                } else {
                    Pair(it, ifTestFalse)
                }
            }.also { items.clear() }

    }

    fun reduceItems(lcm: Long) {
        for (i in items.indices) {
            items[i] = items[i] % lcm
        }
    }

}