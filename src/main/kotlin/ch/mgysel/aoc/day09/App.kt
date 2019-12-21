package ch.mgysel.aoc.day09

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.permute
import ch.mgysel.aoc.common.verify
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

fun main() {
    // part one
    val program = parseInput()

    val resultOne = "TODO"

    println("part one: $resultOne")

    // part two
    val resultTwo = "TODO"

}

fun parseInput(): List<Int> = InputData.read("day09-input.txt")
        .split(",")
        .map(String::toInt)

