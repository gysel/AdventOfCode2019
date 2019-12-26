package ch.mgysel.aoc.day11

import ch.mgysel.aoc.common.Program
import io.kotlintest.shouldBe
import org.junit.Test

class AppKtTest {

    @Test
    fun runPartOne() {
        val code = parseInput()
        val program = Program(code)
        runPartOne(program) shouldBe 2319
    }
}