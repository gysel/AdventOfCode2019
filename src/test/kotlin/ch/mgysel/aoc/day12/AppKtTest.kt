package ch.mgysel.aoc.day12

import io.kotlintest.shouldBe
import org.junit.Test

class AppKtTest {

    @Test
    fun solvePartOne() {
        val moons = input()
        solvePartOne(moons) shouldBe 10944
    }

    @Test
    fun solvePartTwo() {
        val moons = input()
        solvePartTwo(moons) shouldBe 484244804958744
    }

    @Test
    fun providedSample() {
        val moons = listOf(
                Vector3D(-1, 0, 2),
                Vector3D(2, -10, -7),
                Vector3D(4, -8, 8),
                Vector3D(3, 5, -1))
                .map { Moon(it) }

        // After 1 step:
        val stepOne = calculateStep(moons)
        stepOne[0] shouldBe Moon(Vector3D(2, -1, 1), Vector3D(3, -1, -1))
        stepOne[1] shouldBe Moon(Vector3D(3, -7, -4), Vector3D(1, 3, 3))
        stepOne[2] shouldBe Moon(Vector3D(1, -7, 5), Vector3D(-3, 1, -3))
        stepOne[3] shouldBe Moon(Vector3D(2, 2, 0), Vector3D(-1, -3, 1))
    }

    @Test
    fun providedSampleWithPartTwo() {
        val moons = listOf(
                Vector3D(-1, 0, 2),
                Vector3D(2, -10, -7),
                Vector3D(4, -8, 8),
                Vector3D(3, 5, -1))
                .map { Moon(it) }

        solvePartTwo(moons) shouldBe 2772
    }

    @Test
    fun providedSecondSampleWithPartTwo() {
        val moons = listOf(
                Vector3D(-8, -10, 0),
                Vector3D(5, 5, 10),
                Vector3D(2, -7, 3),
                Vector3D(9, -8, -3))
                .map { Moon(it) }

        solvePartTwo(moons) shouldBe 4_686_774_924
    }

    @Test
    fun sumOfVectors() {
        with(Vector3D(x = 1, y = 2, z = 3) + Vector3D(-2, y = 0, z = 3)) {
            x shouldBe -1
            y shouldBe 2
            z shouldBe 6
        }
    }

    @Test
    fun calculateGravity() {
        val ganymede = Vector3D(3, 0, 0)
        val callisto = Vector3D(5, 0, 0)
        with(ganymede.calculateGravity(callisto)) {
            x shouldBe 1
            y shouldBe 0
            z shouldBe 0
        }
        with(callisto.calculateGravity(ganymede)) {
            x shouldBe -1
            y shouldBe 0
            z shouldBe 0
        }
    }
}
