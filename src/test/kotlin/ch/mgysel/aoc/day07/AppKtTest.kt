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
}