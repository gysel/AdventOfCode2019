package ch.mgysel.aoc.day12

import ch.mgysel.aoc.day12.Vector3D.Companion.ZERO
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    val moons = input()

    // part one
    val resultOne = solvePartOne(moons)
    println("part one: $resultOne")

    // part two
}

fun input(): List<Moon> {
    return listOf(
            Vector3D(-3, 10, -1),
            Vector3D(-12, -10, -5),
            Vector3D(-9, 0, 10),
            Vector3D(7, -5, -3)
    ).map { Moon(it) }
}

fun solvePartOne(moons: List<Moon>): Int {
    val result = run(moons, 1000)
    return result
            .sumBy { it.calculateEnergy() }
}

fun run(moons: List<Moon>, steps: Int): List<Moon> {
    var result = moons
    repeat(steps) {
        result = calculateStep(result)
    }
    return result
}

fun calculateStep(moons: List<Moon>): List<Moon> {
    return moons.map { moon ->
        moon.applyGravityAndMove(moons.filter { it != moon })
    }
}

data class Moon(val position: Vector3D,
                val velocity: Vector3D = ZERO) {

    fun applyGravityAndMove(others: List<Moon>): Moon {
        val gravity = others
                .map { other ->
                    position.calculateGravity(other.position)
                }
                .reduce(Vector3D::plus)
        val newVelocity = velocity + gravity
        return Moon(position + newVelocity, newVelocity)
    }

    fun calculateEnergy() = position.absoluteValue() * velocity.absoluteValue()
}

data class Vector3D(val x: Int,
                    val y: Int,
                    val z: Int) {

    operator fun plus(other: Vector3D): Vector3D {
        return Vector3D(x + other.x, y + other.y, z + other.z)
    }

    fun absoluteValue() = x.absoluteValue + y.absoluteValue + z.absoluteValue

    fun calculateGravity(position: Vector3D): Vector3D {
        return Vector3D((position.x - this.x).sign,
                (position.y - this.y).sign,
                (position.z - this.z).sign)
    }

    companion object {
        val ZERO = Vector3D(0, 0, 0)
    }
}
