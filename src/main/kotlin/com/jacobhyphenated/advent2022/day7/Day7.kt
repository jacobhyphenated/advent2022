package com.jacobhyphenated.advent2022.day7

import com.jacobhyphenated.advent2022.Day

/**
 * Day 7: No Space Left On Device
 *
 * To run the system update, your device needs more space.
 * The puzzle input is a list of commands that provide the directory structure and file sizes on the device.
 */
class Day7: Day<List<String>> {
    override fun getInput(): List<String> {
        return readInputFile("day7").lines()
    }

    override fun warmup(input: List<String>): Any {
        return runInputCommands(input)
    }

    /**
     * A directory does not have a size itself. The size is the sum of all its files and directories (recursive)
     * Return the sum of all directories with sizes of at most 100000
     */
    override fun part1(input: List<String>): Int {
        val (_, allDirectories) = runInputCommands(input)
        return allDirectories
            .map { it.size }
            .filter { it <= 100000  }
            .sum()

    }

    /**
     * The total space on the device is 70,000,000. You need 30,000,000 free space to run the update.
     * What is the size of the smallest single directory you can delete that frees up enough space?
     */
    override fun part2(input: List<String>): Int {
        val (root, allDirectories) = runInputCommands(input)
        val freeSpace = 70_000_000 - root.size
        val spaceToDelete = 30_000_000 - freeSpace
        return allDirectories
            .map { it.size }
            .filter { it >= spaceToDelete  }
            .min()
    }

    /**
     * Run through the input commands to build the file system.
     *
     * @param input list of cd and ls commands to use to build out the file system
     * @return The root directory and a list of all directories in the file system.
     * These particular problems involve looking at each directory in the file system,
     * including nested directories. It's useful to return that flat list, so we don't
     * need to recurse the graph structure to look for directories we already know about.
     */
    private fun runInputCommands(input: List<String>): Pair<Directory, List<Directory>> {
        val root = Directory(null, "/")
        var currentDirectory = root
        val allDirectories = mutableListOf(root)
        input.forEach { cmd ->
            val args = cmd.split(" ")
            if (args[0] == "$"){
                // Handle the CD command to change directories
                if (args[1] == "cd") {
                    currentDirectory = when(args[2]) {
                        ".." -> currentDirectory.parent!!
                        "/" -> root
                        else -> currentDirectory.children
                            .filterIsInstance<Directory>()
                            .find { it.name == args[2] }!!
                    }
                }
                // The other command is ls, which prints the FileSystem lines, handled below
            }
            // This line is not a command, so it is the output of ls, either a directory or a file
            else if (args[0] == "dir") {
                val dir = Directory(currentDirectory, args[1])
                allDirectories.add(dir)
                currentDirectory.children.add(dir)
            }
            else {
                val file = File(currentDirectory, args[1], args[0].toInt())
                currentDirectory.children.add(file)
            }
        }
        return Pair(root, allDirectories)
    }
}

sealed class FileSystem(val parent: Directory?, val name: String) {
    abstract val size: Int
}

class File(
    parent: Directory,
    name: String,
    override val size: Int
): FileSystem(parent, name)

class Directory(
    parent: Directory?,
    name: String,
    val children: MutableList<FileSystem> = mutableListOf()
): FileSystem(parent, name) {

    override val size: Int
        get() = children.sumOf { it.size }
}