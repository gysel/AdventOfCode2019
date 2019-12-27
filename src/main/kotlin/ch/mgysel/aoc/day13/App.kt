package ch.mgysel.aoc.day13

import ch.mgysel.aoc.common.Coordinates
import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.Program

fun main() {
    val code = parseInput()
    val program = Program(code)
    val outputs = program.run()

    // part one
    val blocks = findBlocks(outputs)
    val resultOne = blocks.size
    println("part one: $resultOne")

    // part two
    val resultTwo = "TODO"
    println("part two: $resultTwo")
}

fun findBlocks(outputs: List<Long>): Set<Coordinates> {
    return outputs.chunked(3).mapNotNull { chunk ->
        val (x, y, tile) = chunk
        if (tile == 2L) {
            Coordinates(x.toInt(), y.toInt())
        } else null
    }.toSet()
}

fun parseInput(): List<Int> = InputData.read("day13-input.txt")
        .split(",")
        .map(String::toInt)
