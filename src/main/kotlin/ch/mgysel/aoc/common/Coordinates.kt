package ch.mgysel.aoc.common

import ch.mgysel.aoc.common.Direction.*

data class Coordinates(val x: Int,
                       val y: Int)

fun Coordinates.move(direction: Direction): Coordinates = when (direction) {
    UP -> copy(y = y + 1)
    DOWN -> copy(y = y - 1)
    LEFT -> copy(x = x - 1)
    RIGHT -> copy(x = x + 1)
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}
