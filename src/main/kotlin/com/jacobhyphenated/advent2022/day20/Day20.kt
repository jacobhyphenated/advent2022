package com.jacobhyphenated.advent2022.day20

import com.jacobhyphenated.advent2022.Day
import kotlin.math.absoluteValue

/**
 * Day 20: Grove Positioning System
 *
 * Decrypt the coordinates using a custom move encryption.
 *
 * The puzzle input is an array of values that represents a circular list.
 * Using the original order, each value moves a number of spaces in the list equal to its value.
 * Negative numbers move to the left. Wrap-arounds are possible in either direction.
 */
class Day20: Day<List<Long>> {
    override fun getInput(): List<Long> {
        return readInputFile("day20").lines().map { it.toLong() }
    }

    /**
     * Move every value in the input list.
     * Then find the 1000th, 2000th, and 3000th numbers after the 0.
     * return the sum of these numbers.
     */
    override fun part1(input: List<Long>): Long {
        val nodes = initializeNodeList(input)
        doMoveRound(nodes)
        return calculateCoordinates(nodes)
    }

    /**
     * Each value is first multiplied by 811589153.
     * Then the numbers are mixed 10 times.
     * Use the order the numbers originally appeared in for each mixing
     */
    override fun part2(input: List<Long>): Long {
        val nodes = initializeNodeList(input.map { it * 811589153 })
        repeat(10) {
            doMoveRound(nodes)
        }
        return calculateCoordinates(nodes)
    }

    /**
     * Turn our numbers into a circular doubly linked list
     */
    private fun initializeNodeList(input: List<Long>): List<Node> {
        val nodes = input.mapIndexed { i, value -> Node(value, i)}
        val first = nodes.first()
        val last = nodes.last()
        first.previous = last
        last.next = first
        first.next = nodes[1]
        nodes[1].previous = first

        for (i in 1 until nodes.size -1) {
            nodes[i].next = nodes[i+1]
            nodes[i+1].previous = nodes[i]
        }
        return nodes
    }

    /**
     * The list of nodes maintains the original order, regardless of the nodes' positions in the linked list.
     * Go through each node in order and move its position in the list
     */
    private fun doMoveRound(nodes: List<Node>) {
        for (i in nodes.indices) {
            val current = nodes[i]
            val toMove = current.value
            // If we move enough times to wrap around the linked list, we don't have to perform every move
            // because one node is the moving node, the modulo is the list size - 1
            val netMoveAmount =  (toMove.absoluteValue % (nodes.size - 1)).toInt()
            if (toMove < 0) {
                moveLeft(current, netMoveAmount)
            } else {
                moveRight(current, netMoveAmount)
            }
        }
    }

    /**
     * Find the position of the value 0 in the circular array
     * Form a real array from that starting point.
     * Use modulo math to get the 1000th, 2000th, and 3000th values.
     */
    private fun calculateCoordinates(nodes: List<Node>): Long {
        val zero = nodes.first { it.value == 0L }
        var current = zero
        val finalArray = mutableListOf(zero.value)
        while(current.next.originalIndex != zero.originalIndex) {
            finalArray.add(current.next.value)
            current = current.next
        }

        return finalArray[1000 % finalArray.size] + finalArray[2000 % finalArray.size] + finalArray[3000 % finalArray.size]
    }

    /**
     * Move a value in the doubly linked list [amount] positions to the left
     */
    private fun moveLeft(startNode: Node, amount: Int) {
        repeat(amount) {
            val prev = startNode.previous
            prev.previous.next = startNode
            startNode.previous = prev.previous
            startNode.next.previous = prev
            prev.next = startNode.next
            startNode.next = prev
            prev.previous = startNode
        }
    }

    /**
     * Move a value in the doubly linked list [amount] positions to the right
     */
    private fun moveRight(startNode: Node, amount: Int) {
        repeat(amount) {
            val next = startNode.next
            val rhs = next.next
            val lhs = startNode.previous

            lhs.next = next
            next.previous = lhs
            rhs.previous = startNode
            startNode.next = rhs
            next.next = startNode
            startNode.previous = next
        }
    }
}

class Node (val value: Long, val originalIndex: Int) {
    // first time this AoC that I've taken advantage of / abused lateinit
    // 10/10 would abuse this language feature again
    lateinit var next: Node
    lateinit var previous: Node
}