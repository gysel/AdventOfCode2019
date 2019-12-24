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
        val map = SpaceMap(asteroids, 5, 5)
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

    private val map = readPuzzleMap()

    @Test
    fun partOne() {
        val (_, visibleAsteroids) = findAsteroidWithBestVisibility(map)
        visibleAsteroids shouldBe 340
    }

    @Test
    fun partTwo() {
        val monitoringStation = map.readPosition(28, 29)!!
        solvePartTwo(map, monitoringStation) shouldBe 2628
    }

    @Test
    fun calculateVaporizationOrder() {
        val input = """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent()
        val asteroids = input.lines().let(::parseData)
        val map = SpaceMap(asteroids, 20, 20)
        val monitoringStation = map.readPosition(11, 13)!!
        val order = map.calculateVaporizationOrder(monitoringStation)
        order[0].position shouldBe Coordinates(11, 12)
        order[1].position shouldBe Coordinates(12, 1)
        order[2].position shouldBe Coordinates(12, 2)
    }

    @Test
    fun testSortCoordinates() {
        val expected = listOf(
                Coordinates(11, 12),
                Coordinates(12, 1),
                Coordinates(12, 2),
                Coordinates(12, 8),
                Coordinates(16, 0)
        )
        val random = expected.shuffled()
        val center = Coordinates(11, 13)
        val sorted = random.sortedBy {
            center.angleTo(it)
        }
        sorted shouldBe expected
    }

}
