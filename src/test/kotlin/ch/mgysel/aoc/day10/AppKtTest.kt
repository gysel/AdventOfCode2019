package ch.mgysel.aoc.day10

import ch.mgysel.aoc.common.Coordinates
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import org.junit.Test

class AppKtTest {

    @Test
    fun simpleSample() {
        val asteroids = """
            .#..#
            .....
            #####
            ....#
            ...##""".trimIndent().lines().let(::parseData)
        asteroids shouldHaveSize 10
        val map = SpaceMap(asteroids)
        map.readPosition(0, 0) shouldBe null
        val asteroid = map.readPosition(1, 0) ?: throw IllegalStateException("Asteriod expected!")
        val visibleAsteroids = map.findVisibleAsteroidsFrom(asteroid)
        visibleAsteroids.count() shouldBe 7
    }

    @Test
    fun isBetween() {
        val a = Coordinates(1, 0)
        val b = Coordinates(4, 2)
        Coordinates(3, 2).isBetween(a, b) shouldBe false
    }

    @Test
    fun isBetweenWithFloatingPointArithmetic() {
        val a = Coordinates(1, 0)
        val b = Coordinates(4, 3)
        Coordinates(3, 2).isBetween(a, b) shouldBe true
    }

    @Test
    fun partOne() {
        val asteroids = readPuzzleMap()
        val map = SpaceMap(asteroids)
        val (_, visibleAsteroids) = findAsteroidWithBestVisibility(asteroids, map)
        visibleAsteroids shouldBe 340
    }

}
