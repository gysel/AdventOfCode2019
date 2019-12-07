package ch.mgysel.aoc.day04

import io.kotlintest.shouldBe
import org.junit.Test

internal class Day04Test {

    @Test
    fun hasAdjacentDigits() {
        hasAdjacentDigits(11) shouldBe true
        hasAdjacentDigits(12) shouldBe false
        hasAdjacentDigits(12234) shouldBe true
        hasAdjacentDigits(12345) shouldBe false
    }

    @Test
    fun digitsIncrease() {
        digitsIncrease(12345678) shouldBe true
        digitsIncrease(12323456) shouldBe false
    }

    @Test
    fun adjacentDigitsNotPartOfLargerGroup() {
        adjacentDigitsNotPartOfLargerGroup(112233) shouldBe true
        adjacentDigitsNotPartOfLargerGroup(123444) shouldBe false
        adjacentDigitsNotPartOfLargerGroup(111122) shouldBe true
    }
}