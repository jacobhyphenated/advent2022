package com.jacobhyphenated.advent2022

import com.jacobhyphenated.advent2022.day1.Day1
import com.jacobhyphenated.advent2022.day2.Day2
import com.jacobhyphenated.advent2022.day3.Day3
import com.jacobhyphenated.advent2022.day4.Day4
import com.jacobhyphenated.advent2022.day5.Day5
import com.jacobhyphenated.advent2022.day6.Day6
import java.util.*

fun main(args: Array<String>) {
    val days = mapOf(
        "day1" to Day1(),
        "day2" to Day2(),
        "day3" to Day3(),
        "day4" to Day4(),
        "day5" to Day5(),
        "day6" to Day6()
    )
    args.forEach { day ->
        println()
        println(day)
        days[day.lowercase(Locale.getDefault())]?.run() ?: println("No implementation found")
    }
}