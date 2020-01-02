package ch.mgysel.aoc.day14

import io.kotlintest.shouldBe
import org.junit.Ignore
import org.junit.Test

class AppKtTest {

    private val catalog = createReactionCatalog(loadReactions())

    @Test
    fun `part one`() {
        produce("1 FUEL".toChemical(), "ORE", catalog) shouldBe 870051
    }

    @Test
    @Ignore("slow test")
    fun `part two`() {
        solvePartTwo(catalog) shouldBe 1863741
    }

    @Test
    fun `parse reaction from input line`() {
        val line = "3 QVSV => 2 WXRQ"
        parseReaction(line) shouldBe Reaction.parse("2 WXRQ", "3 QVSV")
    }

    @Test
    fun `parse reaction with two inputs`() {
        val line = "2 WXRQ, 12 ZSCZD => 2 HLQM"
        parseReaction(line) shouldBe Reaction.parse("2 HLQM", "2 WXRQ", "12 ZSCZD")
    }

    @Test
    fun `first sample`() {
        val reactions = """
        10 ORE => 10 A
        1 ORE => 1 B
        7 A, 1 B => 1 C
        7 A, 1 C => 1 D
        7 A, 1 D => 1 E
        7 A, 1 E => 1 FUEL
        """.parseReactions()
        produce("1 FUEL".toChemical(), "ORE", reactions) shouldBe 31
    }

    @Test
    fun `second sample`() {
        val reactions = """
            9 ORE => 2 A
            8 ORE => 3 B
            7 ORE => 5 C
            3 A, 4 B => 1 AB
            5 B, 7 C => 1 BC
            4 C, 1 A => 1 CA
            2 AB, 3 BC, 4 CA => 1 FUEL
        """.parseReactions()
        produce("1 FUEL".toChemical(), "ORE", reactions) shouldBe 165
    }

    private fun String.parseReactions() = this.trimIndent().lines()
            .map(::parseReaction)
            .let(::createReactionCatalog)

}