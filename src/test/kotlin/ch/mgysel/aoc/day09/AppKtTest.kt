package ch.mgysel.aoc.day09

import ch.mgysel.aoc.common.Mode
import ch.mgysel.aoc.common.OpCode
import ch.mgysel.aoc.common.Program
import ch.mgysel.aoc.common.parseInstruction
import io.kotlintest.matchers.string.shouldHaveLength
import io.kotlintest.shouldBe
import org.junit.Ignore
import org.junit.Test

class AppKtTest {

    @Test
    fun testParseInstruction() {
        with(parseInstruction(109)) {
            code shouldBe OpCode.ADJUST_REL_BASE
            parameterModes shouldBe listOf(Mode.IMMEDIATE)
        }
        with(parseInstruction(1209)) {
            code shouldBe OpCode.ADJUST_REL_BASE
            parameterModes shouldBe listOf(Mode.RELATIVE, Mode.IMMEDIATE)
        }
    }

    @Test
    fun exampleOne() {
        val code = listOf(
                109, 1, // set relative base to 1
                204, -1, // output with relative mode
                1001, 100, 1, 100,
                1008, 100, 16, 101,
                1006, 101, 0,
                99)
        val program = Program(code)
        program.run() shouldBe code
    }

    @Test
    fun exampleTwo() {
        val code = listOf(1102, 34915192, 34915192, 7, 4, 7, 99, 0)
        val program = Program(code)
        program.run().first().toString() shouldHaveLength 16
    }

    @Test
    fun exampleThree() {
        // TODO use 1125899906842624
        val number = 555555555
        val code = listOf(104, number, 99)
        val program = Program(code)
        program.run().first() shouldBe number
    }

    @Test
    @Ignore
    fun partOne() {
//    private val code = parseInput()
//    private val program = Program(code)
        TODO()
    }

    @Test
    @Ignore
    fun partTwo() {
        TODO()
    }
}