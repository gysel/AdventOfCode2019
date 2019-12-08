package ch.mgysel.aoc.day05

import ch.mgysel.aoc.common.InputData

fun main() {
    // part one
    val data = InputData.read("day05-input.txt")
            .split(",")
            .map(String::toInt)
    val program = data.toMutableList()
    runProgram(program, 1)

    println("part one is done")

    // part two
    val resultTwo = "TODO"
    println("part two: $resultTwo")
}


fun runProgram(program: MutableList<Int>, input: Int): List<Int> {
    var pointer = 0
    while (true) {
        val instruction = parseInstruction(program[pointer])
        when (instruction.code) {
            OpCode.PLUS -> runInstruction(program, pointer, Int::plus, instruction)
            OpCode.MULTIPLY -> runInstruction(program, pointer, Int::times, instruction)
            OpCode.INPUT -> {
                val destination = calculatePosition(instruction, 1, program, pointer)
                program[destination] = input
            }
            OpCode.OUTPUT -> {
                val source = calculatePosition(instruction, 1, program, pointer)
                println("Output: ${program[source]}")
            }
            OpCode.STOP -> return program
        }
        pointer += instruction.code.length
    }
}

private fun runInstruction(program: MutableList<Int>, pointer: Int, operation: (Int, Int) -> Int, instruction: Instruction) {
    val destination = calculatePosition(instruction, 3, program, pointer)
    val first = calculatePosition(instruction, 1, program, pointer)
    val second = calculatePosition(instruction, 2, program, pointer)
    program[destination] = operation.invoke(program[first], program[second])
}

private fun calculatePosition(instruction: Instruction, index: Int, program: MutableList<Int>, pointer: Int): Int {
    return if (instruction.mode(index) == Mode.POSITION) {
        program[pointer + index]
    } else {
        pointer + index
    }
}

fun parseInstruction(instruction: Int): Instruction {
    val string = instruction.toString()
    val opCode = when (string.takeLast(2).toInt()) {
        1 -> OpCode.PLUS
        2 -> OpCode.MULTIPLY
        3 -> OpCode.INPUT
        4 -> OpCode.OUTPUT
        99 -> OpCode.STOP
        else -> throw IllegalStateException("Unexpected OpCode")
    }
    val modes = string.dropLast(2).reversed().map {
        when (it) {
            '0' -> Mode.POSITION
            '1' -> Mode.IMMEDIATE
            else -> throw IllegalStateException("Unexpected mode $it")
        }
    }
    return Instruction(opCode, modes)
}

data class Instruction(val code: OpCode,
                       val parameterModes: List<Mode>) {
    fun mode(parameter: Int): Mode {
        return if (parameterModes.size >= parameter)
            parameterModes[parameter - 1]
        else
            Mode.POSITION
    }
}

enum class Mode {
    POSITION, IMMEDIATE
}

enum class OpCode(val length: Int) {
    PLUS(4),
    MULTIPLY(4),
    INPUT(2),
    OUTPUT(2),
    STOP(1)
}