package ch.mgysel.aoc.day13

import ch.mgysel.aoc.common.Coordinates
import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.Program
import kotlin.math.sign

fun main() {
    val code = parseInput()
    val program = Program(code)

    // part one
    val resultOne = solvePartOne(program)
    println("part one: $resultOne")

    // part two
    runPartTwo(code)
}

fun solvePartOne(program: Program<Int>): Int {
    val outputs = program.run().map(Long::toInt)
    val gameState = GameState()
    gameState.parseDataAndUpdateState(outputs)
    return gameState.countTiles { it is BlockTile }
}

fun runPartTwo(code: List<Int>) {
    val gameState = solvePartTwo(code) { gameState ->
        gameState.printField()
        Thread.sleep(10)
    }
    println("Final Score: ${gameState.currentScore()}")
}

fun solvePartTwo(code: List<Int>, onInput: (GameState) -> Unit = {}): GameState {
    val freeToPlay = createFreeToPlayCode(code)
    val program = Program(freeToPlay)
    val outputs = mutableListOf<Int>()
    val gameState = GameState()
    program.run({
        val ballCoordinates = gameState.findBallPosition()
        val paddleCoordinates = gameState.findPaddlePosition()
        onInput(gameState)
        autoPilot(ballCoordinates, paddleCoordinates)
    }, { output ->
        outputs.add(output.toInt())
        if (outputs.size % 3 == 0) {
            gameState.parseDataAndUpdateState(outputs)
            outputs.clear()
        }
    })
    return gameState
}

abstract class Tile(private val consoleChar: Char) {
    fun toConsole(): String = consoleChar.toString()
}

class EmptyTile : Tile(' ')
class WallTile : Tile('W')
class BlockTile : Tile('X')
class PaddleTile : Tile('-')
class BallTile : Tile('O')

class GameState {
    private val tiles = mutableMapOf<Coordinates, Tile>()
    private var score = 0

    fun parseDataAndUpdateState(data: List<Int>) {
        data.chunked(3).forEach { chunk ->
            val (x, y, code) = chunk
            if (x == -1 && y == 0) {
                score = code
            } else {
                val tile = when (code) {
                    0 -> EmptyTile()
                    1 -> WallTile()
                    2 -> BlockTile()
                    3 -> PaddleTile()
                    4 -> BallTile()
                    else -> throw IllegalStateException("Unexpected tile code: $code")
                }
                tiles[Coordinates(x, y)] = tile
            }
        }

    }

    fun printField() {
        (0..25).map { y ->
            (0..41).joinToString(separator = "") { x ->
                tiles[Coordinates(x, y)]?.toConsole() ?: " "
            }
        }.forEach(::println)
    }

    fun countTiles(identifier: (Tile) -> Boolean): Int = tiles.count { identifier(it.value) }

    fun findBallPosition(): Coordinates = findTilePosition { it is BallTile }

    fun findPaddlePosition(): Coordinates = findTilePosition { it is PaddleTile }

    private fun findTilePosition(identifier: (Tile) -> Boolean): Coordinates {
        return tiles.filter { identifier(it.value) }.keys.first()
    }

    fun currentScore(): Int {
        return score
    }

}

fun autoPilot(ballCoordinates: Coordinates, paddleCoordinates: Coordinates): Int {
    return (ballCoordinates.x - paddleCoordinates.x).sign
}

private fun createFreeToPlayCode(code: List<Int>): List<Int> {
    val freeToPlay = code.toMutableList()
    freeToPlay[0] = 2
    return freeToPlay.toList()
}

fun parseInput(): List<Int> = InputData.read("day13-input.txt")
        .split(",")
        .map(String::toInt)
