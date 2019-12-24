package ch.mgysel.aoc.day10

import ch.mgysel.aoc.common.Coordinates
import ch.mgysel.aoc.common.InputData
import java.lang.Math.toDegrees
import java.util.*
import kotlin.collections.Map.Entry
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    // part one
    val map = readPuzzleMap()

    val (bestAsteroid, visibleAsteroids) = findAsteroidWithBestVisibility(map)
    println("part one: $bestAsteroid can see $visibleAsteroids other asteroids")

    // part two
    val resultTwo = solvePartTwo(map, bestAsteroid)
    println("part two: $resultTwo")
}

 fun solvePartTwo(map: SpaceMap, bestAsteroid: Asteroid): Int {
    val order = map.calculateVaporizationOrder(bestAsteroid)
     return order[199].position
             .let { it.x * 100 + it.y }
}

fun findAsteroidWithBestVisibility(map: SpaceMap): Pair<Asteroid, Int> {
    return (map.asteroids.asSequence()
            .map { it to map.countVisibleAsteroidsFrom(it) }
            .maxBy(Pair<Asteroid, Int>::second)
            ?: throw IllegalStateException("No max found!"))
}

data class Asteroid(val position: Coordinates)

fun readPuzzleMap() = InputData.readLines("day10-input.txt")
        .let { lines ->
            val asteroids = parseData(lines)
            SpaceMap(asteroids, lines.first().length, lines.size)
        }

fun parseData(lines: Iterable<String>): List<Asteroid> {
    return lines.map(String::toCharArray)
            .mapIndexed { y, chars ->
                chars.mapIndexed { x, c ->
                    when (c) {
                        '.' -> null
                        '#' -> Asteroid(Coordinates(x, y))
                        else -> throw IllegalStateException("Unexpected char: $c")
                    }
                }.filterNotNull()
            }.flatten()
}

class SpaceMap(val asteroids: List<Asteroid>,
               private val width: Int,
               private val height: Int) {
    private val map: Map<Coordinates, Asteroid> = asteroids.associateBy(Asteroid::position)

    fun readPosition(x: Int, y: Int) = readPosition(Coordinates(x, y))
    private fun readPosition(coordinates: Coordinates) = map[coordinates]

    fun findVisibleAsteroidsFrom(asteroid: Asteroid): Sequence<Asteroid> {
        val candidates = map
                .filter { entry ->
                    entry.value.position != asteroid.position
                }.map(Entry<Coordinates, Asteroid>::value)
        return candidates.asSequence()
                .filter { candidate ->
                    candidates.none { it.isBetween(asteroid, candidate) }
                }
    }

    fun countVisibleAsteroidsFrom(asteroid: Asteroid) =
            findVisibleAsteroidsFrom(asteroid).count()

    fun calculateVaporizationOrder(monitoringStation: Asteroid): List<Asteroid> {
        val origin = monitoringStation.position

        val vaporizationOrder = asteroids
                .groupByTo(TreeMap<Int, MutableList<Asteroid>>()) {
                    origin.angleTo(it.position)
                            // reduce precision to work around floating point problems
                            .times(10000).toInt()
                }
        val vaporizedAsteroids = mutableListOf<Asteroid>()
        vaporizationOrder.forEach { (_, asteroidsAtAngle) ->
            if (asteroidsAtAngle.isNotEmpty()) {
                asteroidsAtAngle.sortBy { it.position.distanceTo(origin) }
                vaporizedAsteroids.add(asteroidsAtAngle.removeAt(0))
            }
        }
        return vaporizedAsteroids
    }

}

fun Asteroid.isBetween(a: Asteroid, b: Asteroid): Boolean {
    return this.position.isBetween(a.position, b.position)
}

fun Coordinates.isBetween(a: Coordinates, b: Coordinates): Boolean {
    if (this == a || this == b) return false
    return ((a.distanceTo(this) + distanceTo(b)) - a.distanceTo(b)).absoluteValue < 0.00001
}

fun Coordinates.distanceTo(other: Coordinates) =
        sqrt((this.x.toDouble() - other.x).pow(2.0) + (this.y.toDouble() - other.y).pow(2.0))

/**
 * Shamelessly stolen from https://todd.ginsberg.com/post/advent-of-code/2019/day10/
 */
fun Coordinates.angleTo(other: Coordinates): Double {
    val d = toDegrees(
            atan2(
                    (other.y - y).toDouble(),
                    (other.x - x).toDouble()
            )
    ) + 90
    return if (d < 0) d + 360 else d
}
