package ch.mgysel.aoc.day13

import ch.mgysel.aoc.common.Program
import io.kotlintest.matchers.collections.shouldHaveSize
import org.junit.Test

class AppKtTest {

    @Test
    fun solvePartOne() {
        val program = Program(parseInput())
        findBlocks(program.run()) shouldHaveSize 380
    }
}