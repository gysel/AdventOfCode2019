package ch.mgysel.aoc.day03

import ch.mgysel.aoc.common.InputData
import kotlin.math.absoluteValue

fun main() {
    val data = InputData.readLines("day03-input.txt")
            .map { it.split(",") }
    // part one
    val origin = Coordinates(0, 0)
    data.forEachIndexed() { cable, moves ->
        var position = origin
        moves.forEach { move ->
            val count = move.drop(1).toInt()
            val direction = when (move.first()) {
                'L' -> Direction.LEFT
                'R' -> Direction.RIGHT
                'U' -> Direction.UP
                'D' -> Direction.DOWN
                else -> throw IllegalStateException("Unexpected direction!")
            }
            position = Field.add(position, direction, count, cable)
        }
    }
    val intersections = Field.findIntersections()
    println("Intersections: $intersections")
    val resultOne = intersections.map { calculateManhattanDistance(origin, it) }.min()
    println("part one: $resultOne")
    // part two
    val resultTwo = "todo"
    println("part two: $resultTwo")
}

object Field {
    private val points: MutableMap<Coordinates, MutableSet<Int>> = mutableMapOf()

    fun findIntersections(): Set<Coordinates> {
        return points.filter { it.value.size > 1 }.keys
    }

    fun add(start: Coordinates, direction: Direction, count: Int, cable: Int): Coordinates {
        var position = start
        repeat(count) {
            position = position.move(direction)
            val cables = points.getOrDefault(position, mutableSetOf())
            cables.add(cable)
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