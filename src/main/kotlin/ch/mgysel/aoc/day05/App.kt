package ch.mgysel.aoc.day05

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.Program

fun main() {
    // part one
    val code = readInput()
    val program = Program(code)

    val output = solve(program, 1)
    println("part one: $output")

    // part two
    val resultTwo = solve(program, 5)
    println("part two: $resultTwo")
}

fun solve(program: Program<Int>, input: Int): Int? {
    return program.run(input)?.toInt()
}

fun readInput(): List<Int> = InputData.read("day05-input.txt")
        .split(",")
        .map(String::toInt)

