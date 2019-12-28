package ch.mgysel.aoc.day13

import ch.mgysel.aoc.common.Program
import io.kotlintest.shouldBe
import org.junit.Test

class AppKtTest {

    @Test
    fun solvePartOne() {
        val code = parseInput()
        val program = Program(code)
        solvePartOne(program) shouldBe 380
    }

    @Test
    fun solvePartTwo() {
        val code = parseInput()
        solvePartTwo(code).currentScore() shouldBe 18647
    }
}