package ch.mgysel.aoc.day10

import ch.mgysel.aoc.common.Coordinates
import ch.mgysel.aoc.common.InputData
import kotlin.collections.Map.Entry
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    // part one
    val asteroids = readPuzzleMap()
    println("found ${asteroids.size} asteriods")

    val map = SpaceMap(asteroids)
    val (bestAsteroid, visibleAsteroids) = findAsteroidWithBestVisibility(asteroids, map)
    println("part one: $bestAsteroid can see $visibleAsteroids other asteroids")

    // part two

    val resultTwo = "TODO"
    println("part two: $resultTwo")
}

fun findAsteroidWithBestVisibility(asteroids: List<Asteroid>, map: SpaceMap): Pair<Asteroid, Int> {
    return (asteroids.asSequence()
            .map { it to map.countVisibleAsteroidsFrom(it) }
            .maxBy(Pair<Asteroid, Int>::second)
            ?: throw IllegalStateException("No max found!"))
}

data class Asteroid(val position: Coordinates)

fun readPuzzleMap() = InputData.readLines("day10-input.txt")
        .let(::parseData)

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

class SpaceMap(asteroids: List<Asteroid>) {
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