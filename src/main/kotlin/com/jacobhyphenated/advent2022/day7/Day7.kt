package com.jacobhyphenated.advent2022.day7

import com.jacobhyphenated.advent2022.Day

class Day7: Day<List<String>> {
    override fun getInput(): List<String> {
        return readInputFile("day7").lines()
    }

    override fun warmup(input: List<String>): Any {
        return runInputCommands(input)
    }

    override fun part1(input: List<String>): Int {
        val allDirectories = runInputCommands(input)
        return allDirectories
            .map { it.size() }
            .filter { it <= 100000  }
            .sum()

    }

    override fun part2(input: List<String>): Int {
        val allDirectories = runInputCommands(input)
        val root = allDirectories.find { it.name == "/" }!!
        val freeSpace = 70_000_000 - root.size()
        val spaceToDelete = 30_000_000 - freeSpace
        return allDirectories
            .map { it.size() }
            .filter { it >= spaceToDelete  }
            .min()
    }

    private fun runInputCommands(input: List<String>): List<Directory> {
        val root = Directory(null, "/")
        var currentDirectory = root
        var index = 0
        val allDirectories = mutableListOf(root)
        while (index < input.size) {
            val args = input[index].split(" ")
            if (args[0] == "$"){
                if (args[1] == "cd") {
                    currentDirectory = when(args[2]) {
                        ".." -> currentDirectory.parent!!
                        "/" -> root
                        else -> currentDirectory.children.find { it.name == args[2] }!! as Directory
                    }
                }
            }
            else if (args[0] == "dir") {
                val dir = Directory(currentDirectory, args[1])
                allDirectories.add(dir)
                currentDirectory.children.add(dir)
            }
            else {
                val file = File(currentDirectory, args[1], args[0].toInt())
                currentDirectory.children.add(file)
            }
            index++
        }
        return allDirectories
    }
}

abstract class FileSystem(val parent: Directory?, val name: String) {
    abstract fun size(): Int
}

class File(
    parent: Directory,
    name: String,
    private val size: Int
): FileSystem(parent, name) {

    override fun size(): Int {
        return size
    }
}

class Directory(
    parent: Directory?,
    name: String,
    val children: MutableList<FileSystem> = mutableListOf()
): FileSystem(parent, name) {

    override fun size(): Int {
        return children.sumOf { it.size() }
    }
}