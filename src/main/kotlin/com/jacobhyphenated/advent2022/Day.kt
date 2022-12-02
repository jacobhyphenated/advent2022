package com.jacobhyphenated.advent2022

interface Day<T> {
    fun getInput(): T
    fun part1(input: T): Number
    fun part2(input: T): Number

    fun run() {
        val input = getInput()
        var start = System.nanoTime()
        println("Part 1: ${part1(input)} (${(System.nanoTime() - start) / 1000000.0}ms)")
        start = System.nanoTime()
        println("Part 2: ${part2(input)} (${(System.nanoTime() - start) / 1000000.0}ms)")
    }

    fun readInputFile(day: String): String {
        return this.javaClass.classLoader.getResource("$day/input.txt")!!
            .readText()
    }
}