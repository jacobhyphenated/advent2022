package com.jacobhyphenated.advent2022

interface Day<T> {
    fun getInput(): T
    fun part1(input: T): Any
    fun part2(input: T): Any

    fun run() {
        val input = getInput()
        warmup(input)
        var start = System.nanoTime()
        println("Part 1: ${part1(input)} (${(System.nanoTime() - start) / 1_000_000.0}ms)")
        start = System.nanoTime()
        println("Part 2: ${part2(input)} (${(System.nanoTime() - start) / 1_000_000.0}ms)")
    }

    fun readInputFile(day: String): String {
        return this.javaClass.classLoader.getResource("$day/input.txt")!!
            .readText()
    }

    /**
     * Run this code before performance testing. Use this to run code to trigger compiler optimization.
     *
     * The default is a no-op method. Override to provide Day specific optimization code.
     *
     * Any code executed in this method will not be timed during part 1 or part 2
     */
    fun warmup(input: T): Any {
        //No-op
        return 0
    }
}