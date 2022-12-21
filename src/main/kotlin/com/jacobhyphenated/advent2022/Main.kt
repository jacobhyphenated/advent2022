package com.jacobhyphenated.advent2022

import com.jacobhyphenated.advent2022.day1.Day1
import com.jacobhyphenated.advent2022.day10.Day10
import com.jacobhyphenated.advent2022.day11.Day11
import com.jacobhyphenated.advent2022.day12.Day12
import com.jacobhyphenated.advent2022.day13.Day13
import com.jacobhyphenated.advent2022.day14.Day14
import com.jacobhyphenated.advent2022.day15.Day15
import com.jacobhyphenated.advent2022.day16.Day16
import com.jacobhyphenated.advent2022.day17.Day17
import com.jacobhyphenated.advent2022.day18.Day18
import com.jacobhyphenated.advent2022.day19.Day19
import com.jacobhyphenated.advent2022.day2.Day2
import com.jacobhyphenated.advent2022.day20.Day20
import com.jacobhyphenated.advent2022.day21.Day21
import com.jacobhyphenated.advent2022.day22.Day22
import com.jacobhyphenated.advent2022.day23.Day23
import com.jacobhyphenated.advent2022.day24.Day24
import com.jacobhyphenated.advent2022.day25.Day25
import com.jacobhyphenated.advent2022.day3.Day3
import com.jacobhyphenated.advent2022.day4.Day4
import com.jacobhyphenated.advent2022.day5.Day5
import com.jacobhyphenated.advent2022.day6.Day6
import com.jacobhyphenated.advent2022.day7.Day7
import com.jacobhyphenated.advent2022.day8.Day8
import com.jacobhyphenated.advent2022.day9.Day9
import java.util.*

fun main(args: Array<String>) {
    val days = mapOf(
        "day1" to Day1(),
        "day2" to Day2(),
        "day3" to Day3(),
        "day4" to Day4(),
        "day5" to Day5(),
        "day6" to Day6(),
        "day7" to Day7(),
        "day8" to Day8(),
        "day9" to Day9(),
        "day10" to Day10(),
        "day11" to Day11(),
        "day12" to Day12(),
        "day13" to Day13(),
        "day14" to Day14(),
        "day15" to Day15(),
        "day16" to Day16(),
        "day17" to Day17(),
        "day18" to Day18(),
        "day19" to Day19(),
        "day20" to Day20(),
        "day21" to Day21(),
        "day22" to Day22(),
        "day23" to Day23(),
        "day24" to Day24(),
        "day25" to Day25(),
    )
    args.forEach { day ->
        println()
        println(day)
        days[day.lowercase(Locale.getDefault())]?.run() ?: println("No implementation found")
    }
}