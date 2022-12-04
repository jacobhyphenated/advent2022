package com.jacobhyphenated.advent2022

import com.jacobhyphenated.advent2022.day1.Day1
import com.jacobhyphenated.advent2022.day2.Day2
import com.jacobhyphenated.advent2022.day3.Day3
import com.jacobhyphenated.advent2022.day4.Day4
import java.util.*

fun main(args: Array<String>) {
    val days = mapOf(
        "day1" to Day1(),
        "day2" to Day2(),
        "day3" to Day3(),
        "day4" to Day4()
    )
    args.forEach { day ->
        println()
        println(day)
        days[day.lowercase(Locale.getDefault())]?.run() ?: println("No implementation found")
    }
}