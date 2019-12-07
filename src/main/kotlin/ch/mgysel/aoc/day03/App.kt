package ch.mgysel.aoc.day03

import ch.mgysel.aoc.common.InputData
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.absoluteValue

fun main() {
    val data = InputData.readLines("day03-input.txt")
            .map { it.split(",") }
    // part one
    val origin = Coordinates(0, 0)
    data.forEachIndexed() { cable, moves ->
        var position = origin
        val length = AtomicInteger(0)
        moves.forEach { move ->
            val direction = parseDirection(move.first())
            val count = move.drop(1).toInt()
            position = Field.add(position, direction, count, cable, length)
        }
    }
    val intersections = Field.findIntersections()
    println("Intersections: ${intersections.size}")
    val resultOne = intersections.map { intersection -> calculateManhattanDistance(origin, intersection.key) }.min()
    println("part one: $resultOne")
    // part two
    val resultTwo = intersections.map { it.key to it.value.map(CableWaypoint::cableLength).sum() }
            .minBy { it.second }
            ?.let { it.second }
            ?: throw IllegalStateException("No minimum found!")

    println("part two: $resultTwo")
}

private fun parseDirection(character: Char): Direction {
    return when (character) {
        'L' -> Direction.LEFT
        'R' -> Direction.RIGHT
        'U' -> Direction.UP
        'D' -> Direction.DOWN
        else -> throw IllegalStateException("Unexpected direction!")
    }
}

object Field {
    private val points: MutableMap<Coordinates, MutableSet<CableWaypoint>> = mutableMapOf()

    fun findIntersections(): Map<Coordinates, Set<CableWaypoint>> {
        return points.filter { point ->
            point.value.map(CableWaypoint::cable).distinct().size > 1
        }
    }

    fun add(start: Coordinates, direction: Direction, count: Int, cable: Int, length: AtomicInteger): Coordinates {
        var position = start
        repeat(count) {
            position = position.move(direction)
            val cables = points.getOrDefault(position, mutableSetOf())
            cables.add(CableWaypoint(cable, length.incrementAndGet()))
            points[position] = cables
        }
        return position
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class Coordinates(val x: Int,
                       val y: Int) {
    fun move(direction: Direction): Coordinates = when (direction) {
        Direction.UP -> copy(y = y + 1)
        Direction.DOWN -> copy(y = y - 1)
        Direction.LEFT -> copy(x = x - 1)
        Direction.RIGHT -> copy(x = x + 1)
    }
}

fun calculateManhattanDistance(a: Coordinates, b: Coordinates): Int {
    return (a.x - b.x).absoluteValue + (a.y - b.y).absoluteValue
}

data class CableWaypoint(val cable: Int,
                         val cableLength: Int)
