package ch.mgysel.aoc.day11

import ch.mgysel.aoc.common.*

fun main() {
    // part one
    val code = parseInput()

    val program = Program(code)
    val robot = HullPainingRobot()
    var counter = 0
    program.run({
        robot.readCurrentColor().code.toLong()
    }, {
        val operation = it.toInt()
        if (counter % 2 == 0) {
            robot.paint(operation)
        } else {
            robot.turn(operation)
            robot.move()
        }
        counter++
    })

    val resultOne = robot.countPaintedTiles()

    println("part one: $resultOne")

    // part two
    val resultTwo = "TODO"
    println("part two: $resultTwo")

}

fun Direction.turnLeft() = when (this) {
    Direction.UP -> Direction.LEFT
    Direction.RIGHT -> Direction.UP
    Direction.DOWN -> Direction.RIGHT
    Direction.LEFT -> Direction.DOWN
}

fun Direction.turnRight() = when (this) {
    Direction.UP -> Direction.RIGHT
    Direction.RIGHT -> Direction.DOWN
    Direction.DOWN -> Direction.LEFT
    Direction.LEFT -> Direction.UP
}

enum class Color(val code: Int) {
    BLACK(0), WHITE(1);

    companion object {
        fun fromCode(code: Int) = values().find { it.code == code }
    }
}

class HullPainingRobot {
    private var position: Coordinates = Coordinates(0, 0)
    private var orientation: Direction = Direction.UP
    private val paintedTiles = mutableMapOf<Coordinates, Color>()

    fun move() {
        position = position.move(orientation)
    }

    fun turn(direction: Int) {
        orientation = when (direction) {
            0 -> orientation.turnLeft()
            1 -> orientation.turnRight()
            else -> throw IllegalStateException("Unexpected direction $direction")
        }
    }

    fun paint(colorCode: Int) {
        val color = Color.fromCode(colorCode)
                ?: throw IllegalStateException("Unknown color code $colorCode")
        paintedTiles[position] = color
    }

    fun readCurrentColor() = paintedTiles.getOrDefault(position, Color.BLACK)

    fun countPaintedTiles() = paintedTiles.size
}

fun parseInput(): List<Long> = InputData.read("day11-input.txt")
        .split(",")
        .map(String::toLong)
