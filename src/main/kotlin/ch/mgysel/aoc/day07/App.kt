package ch.mgysel.aoc.day07

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.permute
import java.util.*

fun main() {
    // part one
    val program = InputData.read("day07-input.txt")
            .split(",")
            .map(String::toInt)

    val settingsSets = permute((0..4).toList())
    val maxOutput = settingsSets
            .map { settings ->
                val signal = settings.fold(0) { previousOutput, parameter ->
                    val inputs = listOf(parameter, previousOutput)
                    runProgram(program, inputs)
                            ?: throw IllegalStateException("No output found!")
                }
                Combination(settings, signal)
            }.maxBy(Combination::signal)

    val output = "$maxOutput"
    println("part one: $output")

    // part two
    val resultTwo = "TODO"
    println("part two: $resultTwo")
}

data class Combination(val settings: List<Int>,
                       val signal: Int)


fun runProgram(inputProgram: List<Int>, inputs: List<Int>): Int? {
    val program = inputProgram.toMutableList()
    val inputQueue = LinkedList(inputs)
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
                program[destination] = inputQueue.pop()
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