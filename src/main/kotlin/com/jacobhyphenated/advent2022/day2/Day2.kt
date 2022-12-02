package com.jacobhyphenated.advent2022.day2

import com.jacobhyphenated.advent2022.Day

/**
 * Day 2: Rock Paper Scissors
 *
 * To cheat at a tournament of paper, rock, scissors, you have a puzzle input of two columns.
 * The first column consists of "A" = Rock, "B" = Paper, and "C" = Scissors
 *
 * Each round is scored by adding up the result: loss = 0, draw = 3, win = 6
 * and the Move you (the second column) take: Rock = 1, Paper = 2, Scissors = 3
 */
class Day2: Day<List<String>> {
    override fun getInput(): List<String> {
        return readInputFile("day2").lines()
    }

    /**
     * Assume the second column is the move you are supposed to take where X = Rock, Y = Paper, Z = Scissors.
     * What is the final score after adding up the score for each round?
     */
    override fun part1(input: List<String>): Number {
        return input.map {
            val (move1, move2) = it.split(" ")
            Pair(parseMove(move1), parseMove(move2))
        }.sumOf { scoreRound(it) }
    }

    /**
     * It turns out the second column was the expected result of the round for you.
     * X = Loss, Y = Draw, Z = Win.
     *
     * What is the final score after adding up the score for each round?
     */
    override fun part2(input: List<String>): Number {
        return input.map {
            val (move1, expectedResult) = it.split(" ")
            val firstMove = parseMove(move1)
            Pair(firstMove, determineSecondMove(expectedResult, firstMove))
        }.sumOf { scoreRound(it) }
    }

    private fun scoreRound(round: Pair<Move,Move>): Int {
        val (opponentMove, yourMove) = round
        return roundResultScore(opponentMove, yourMove) + yourMove.points
    }

    private fun parseMove(move: String): Move {
        return when (move) {
            "A", "X" -> Move.ROCK
            "B", "Y" -> Move.PAPER
            "C", "Z" -> Move.SCISSORS
            else -> throw NotImplementedError("Invalid character $move")
        }
    }

    /**
     * Given the move your opponent will make (firstMove) and the expected result (Win, loss, draw),
     * determine what move you make this round.
     * @param expectedResult X = loss, Y = draw, Z = win
     * @param firstMove the move your opponent is making
     * @return the move you must make to get the expected result
     */
    private fun determineSecondMove(expectedResult: String, firstMove: Move): Move {
        return when(expectedResult) {
            "X" -> when (firstMove) {
                Move.PAPER -> Move.ROCK
                Move.SCISSORS -> Move.PAPER
                Move.ROCK -> Move.SCISSORS
            }
            "Y" -> firstMove
            "Z" -> when (firstMove) {
                Move.PAPER -> Move.SCISSORS
                Move.SCISSORS -> Move.ROCK
                Move.ROCK -> Move.PAPER
            }
            else -> throw NotImplementedError("Invalid character $expectedResult")
        }
    }

    /**
     * Given your move and the opponents move, determine how many points you get.
     * loss = 0, draw = 3, win = 6
     *
     * Uses the standard paper, rock, scissors rules
     */
    private fun roundResultScore(opponent: Move, you: Move): Int {
        return when(opponent){
            Move.SCISSORS -> when (you) {
                Move.SCISSORS -> 3
                Move.ROCK -> 6
                Move.PAPER -> 0
            }
            Move.PAPER -> when (you) {
                Move.SCISSORS -> 6
                Move.PAPER -> 3
                Move.ROCK -> 0
            }
            Move.ROCK -> when (you) {
                Move.SCISSORS -> 0
                Move.ROCK -> 3
                Move.PAPER -> 6
            }
        }
    }
}

enum class Move(val points: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

}