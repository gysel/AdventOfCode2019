package ch.mgysel.aoc.day05

import ch.mgysel.aoc.common.InputData

fun main() {
    // part one
    val data = InputData.read("day05-input.txt")
            .split(",")
            .map(String::toInt)
    val output = runProgram(data.toMutableList(), 1)
    println("part one: $output")

    // part two
    val resultTwo = runProgram(data.toMutableList(), 5)
    println("part two: $resultTwo")
}


fun runProgram(program: MutableList<Int>, input: Int): Int? {
    var pointer = 0
    var output: Int? = null
    while (true) {
        val instruction = parseInstruction(program[pointer])
        var nextPointer = pointer + instruction.code.length
        when (instruction.code) {
            OpCode.PLUS -> runInstruction(program, pointer, Int::plus, instruction)
            OpCode.MULTIPLY -> runInstruction(program, pointer, Int::times, instruction)
            OpCode.INPUT -> {
                val destination = calculatePosition(instruction, 1, program, pointer)
                program[destination] = input
            }
            OpCode.OUTPUT -> {
                val source = calculatePosition(instruction, 1, program, pointer)
                output = program[source]
            }
            OpCode.JUMP_IF_TRUE -> {
                val test = calculatePosition(instruction, 1, program, pointer)
                if (program[test] != 0) {
                    nextPointer = program[calculatePosition(instruction, 2, program, pointer)]
                }
            }
            OpCode.JUMP_IF_FALSE -> {
                val test = calculatePosition(instruction, 1, program, pointer)
                if (program[test] == 0) {
                    nextPointer = program[calculatePosition(instruction, 2, program, pointer)]
                }
            }
            OpCode.LESS_THAN -> {
                val first = calculatePosition(instruction, 1, program, pointer)
                val second = calculatePosition(instruction, 2, program, pointer)
                val destination = calculatePosition(instruction, 3, program, pointer)
                program[destination] = if (program[first] < program[second]) {
                    1
                } else {
                    0
                }
            }
            OpCode.EQUALS -> {
                val first = calculatePosition(instruction, 1, program, pointer)
                val second = calculatePosition(instruction, 2, program, pointer)
                val destination = calculatePosition(instruction, 3, program, pointer)
                program[destination] = if (program[first] == program[second]) {
                    1
                } else {
                    0
                }
            }
            OpCode.STOP -> return output
        }
        pointer = nextPointer
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
        5 -> OpCode.JUMP_IF_TRUE
        6 -> OpCode.JUMP_IF_FALSE
        7 -> OpCode.LESS_THAN
        8 -> OpCode.EQUALS
        99 -> OpCode.STOP
        else -> throw IllegalStateException("Unexpected OpCode $string")
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
    STOP(1),
    JUMP_IF_TRUE(3),
    JUMP_IF_FALSE(3),
    LESS_THAN(4),
    EQUALS(4)
}