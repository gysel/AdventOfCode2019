package ch.mgysel.aoc.day07

import ch.mgysel.aoc.common.Program
import io.kotlintest.shouldBe
import org.junit.Test

class AppKtTest {
    private val code = parseInput()
    private val program = Program(code)

    @Test
    fun partOne() {
        val maxOutput = solvePartOne(program)
        maxOutput.signal shouldBe 67023
    }

    @Test
    fun partTwo() {
        val resultTwo = solvePartTwo(program)
        resultTwo.signal shouldBe 7818398
    }

    @Test
    fun sampleOne() {
        val testCode = listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0)
        val testProgram = Program(testCode)
        val settings = listOf(4, 3, 2, 1, 0)
        runProgram(settings, testProgram).signal shouldBe 43210
    }
    @Test
    fun sampleTwo() {
        val testCode = listOf(3,23,3,24,1002,24,10,24,1002,23,-1,23,
                101,5,23,23,1,24,23,23,4,23,99,0,0)
        val testProgram = Program(testCode)
        val settings = listOf(0,1,2,3,4)
        runProgram(settings, testProgram).signal shouldBe 54321
    }
    @Test
    fun sampleThree() {
        val testCode = listOf(3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,
                1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0)
        val testProgram = Program(testCode)
        val settings = listOf(1,0,4,3,2)
        runProgram(settings, testProgram).signal shouldBe 65210
    }
}