package ch.mgysel.aoc.day02

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.cartesianProduct

fun main() {
    val data = InputData.read("day02-input.txt")
            .split(",")
            .map(String::toInt)
    // part one
    val result = runProgram(data, 12, 2)
    println("part one: ${result.first()}")
    // part two
    val solution = cartesianProduct(1..99, 1..99)
            .first { params ->
                19690720 == runProgram(data, params.first, params.second).first()
            }
    val answer = solution.let { 100 * it.first + it.second }
    println("part two: $answer // $solution")
}

private fun runProgram(data: List<Int>, noun: Int, verb: Int): List<Int> {
    val program = data.toMutableList()
    program[1] = noun
    program[2] = verb
    return runProgram(program)
}

private fun runProgram(program: MutableList<Int>): List<Int> {
    var pointer = 0
    while (true) {
        pointer = when (program[pointer]) {
            1 -> runInstruction(program, pointer, Int::plus)
            2 -> runInstruction(program, pointer, Int::times)
            99 -> return program
            else -> {
                throw IllegalStateException("Unknown instruction ${program[pointer]}")
            }
        }
    }
}

private fun runInstruction(program: MutableList<Int>, pointer: Int, operation: (Int, Int) -> Int): Int {
    val destination = program[pointer + 3]
    program[destination] = operation.invoke(program[program[pointer + 1]], program[program[pointer + 2]])
    return pointer + 4
}
