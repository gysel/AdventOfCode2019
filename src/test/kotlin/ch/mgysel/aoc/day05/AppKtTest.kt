package ch.mgysel.aoc.day05

import io.kotlintest.shouldBe
import org.junit.Ignore
import org.junit.Test

class AppKtTest {

    @Test
    fun parseInstruction() {
        with(parseInstruction(1002)) {
            code shouldBe OpCode.MULTIPLY
            parameterModes shouldBe listOf(Mode.POSITION, Mode.IMMEDIATE)
        }
        parseInstruction(1001).code shouldBe OpCode.PLUS
        parseInstruction(1002).code shouldBe OpCode.MULTIPLY
    }

    @Test
    fun sampleProgram() {
        val data = listOf(3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99)
        runProgram(data.toMutableList(), 7)
    }

    @Test
    fun test1() {
        val data = listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)
        runProgram(data.toMutableList(), 7) shouldBe 0
        runProgram(data.toMutableList(), 8) shouldBe 1
        runProgram(data.toMutableList(), 9) shouldBe 0
    }

    @Test
    fun test2() {
        val data = listOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8)
        runProgram(data.toMutableList(), 7) shouldBe 1
        runProgram(data.toMutableList(), 8) shouldBe 0
        runProgram(data.toMutableList(), 9) shouldBe 0
    }

    @Test
    fun test3() {
        val data = listOf(3, 3, 1108, -1, 8, 3, 4, 3, 99)
        runProgram(data.toMutableList(), 7) shouldBe 0
        runProgram(data.toMutableList(), 8) shouldBe 1
        runProgram(data.toMutableList(), 9) shouldBe 0
    }

    @Test
    fun test4() {
        val data = listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99)
        runProgram(data.toMutableList(), 7) shouldBe 1
        runProgram(data.toMutableList(), 8) shouldBe 0
        runProgram(data.toMutableList(), 9) shouldBe 0
    }

    @Test
    fun testJump1() {
        val data = listOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9)
        runProgram(data.toMutableList(), 0) shouldBe 0
        runProgram(data.toMutableList(), 1) shouldBe 1
        runProgram(data.toMutableList(), 2) shouldBe 1
    }

    @Test
    fun testJump2() {
        val data = listOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1)
        runProgram(data.toMutableList(), 0) shouldBe 0
        runProgram(data.toMutableList(), 1) shouldBe 1
        runProgram(data.toMutableList(), 2) shouldBe 1
    }
}
