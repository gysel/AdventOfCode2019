package ch.mgysel.aoc.day09

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.Program

fun main() {
    // part one
    val code = parseInput()

    val program = Program(code)
    val resultOne = program.run(1)

    println("part one: $resultOne")

    // part two
    val resultTwo = program.run(2)
    println("part two: $resultTwo")

}

fun parseInput(): List<Int> = InputData.read("day09-input.txt")
        .split(",")
        .map(String::toInt)

