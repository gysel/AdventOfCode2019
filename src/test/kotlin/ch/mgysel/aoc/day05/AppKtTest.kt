package ch.mgysel.aoc.day05

import io.kotlintest.shouldBe
import org.junit.Ignore
import org.junit.Test

class AppKtTest {

    @Test
    @Ignore
    fun runProgram() {
        val data = listOf(1002, 4, 3, 4, 33).toMutableList()
        runProgram(data)
    }

    @Test
    fun parseInstruction() {
        with(parseInstruction(1002)) {
            code shouldBe OpCode.MULTIPLY
            parameterModes shouldBe listOf(Mode.POSITION, Mode.IMMEDIATE)
        }
        parseInstruction(1001) shouldBe OpCode.MULTIPLY
    }

}
