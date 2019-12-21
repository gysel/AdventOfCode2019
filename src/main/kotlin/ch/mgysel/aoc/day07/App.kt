package ch.mgysel.aoc.day07

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.Program
import ch.mgysel.aoc.common.permute
import ch.mgysel.aoc.common.verify
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

fun main() {
    val code = parseInput()
    val program = Program(code)

    // part one
    val maxOutput = solvePartOne(program)
    println("part one: $maxOutput")

    // part two
    val resultTwo = solvePartTwo(program)
    println("part two: $resultTwo")
}

fun parseInput(): List<Int> = InputData.read("day07-input.txt")
        .split(",")
        .map(String::toInt)


fun solvePartOne(program: Program<Int>): Combination {
    val settingsSets = permute((0..4).toList())
    return settingsSets
            .map { settings ->
                runProgram(settings, program)
            }.maxBy(Combination::signal) ?: throw IllegalStateException("No maximum found!")
}

fun runProgram(settings: List<Int>, program: Program<Int>): Combination {
    val signal = settings.fold(0) { previousOutput, parameter ->
        val inputs = listOf(parameter, previousOutput)
        val inputQueue = LinkedList(inputs)
        var output: Int? = null
        program.run({ inputQueue.pop() }, { output = it.toInt() })
        output ?: throw IllegalStateException("No output found!")
    }
    return Combination(settings, signal)
}

fun solvePartTwo(program: Program<Int>): Combination {
    val pool = Executors.newFixedThreadPool(5)
    val resultTwo = permute((5..9).toList())
            .map { settings ->
                // prepare initial settings
                val handovers = (0..4).map { i -> LinkedBlockingQueue<Int>(listOf(settings[i])) }
                // add initial value to first amplifier
                handovers.first().add(0)
                // run 5 programs in parallel
                val programs: List<Runnable> = ('A'..'E').mapIndexed { i, _ ->
                    Runnable {
                        program.run({
                            handovers[i].take()
                        }) {
                            handovers[(i + 1) % 5].put(it.toInt())
                        }
                    }
                }
                programs.map(pool::submit).forEach { it.get() }
                // read the last signal from the handover of E -> A
                val signal = handovers.first().first()
                Combination(settings, signal)
            }
            .maxBy(Combination::signal)
            ?: throw IllegalStateException("no max found!!")
    pool.shutdown()
    return resultTwo
}

data class Combination(val settings: List<Int>,
                       val signal: Int)

