package ch.mgysel.aoc.day08

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.verify

fun main() {
    // part one
    val pixels = InputData.read("day08-input.txt").map { char -> char.toString().toInt() }
    val width = 25
    val height = 6
    val pixelsPerLayer = width * height
    verify(pixels.size % pixelsPerLayer, 0, "uneven pixel number")
    val layers = pixels.chunked(pixelsPerLayer)
    println("Found ${layers.size} layers")

    val countZero = { layer: List<Int> -> layer.count(0) }
    val layer = layers.minBy(countZero) ?: throw IllegalStateException("No layer found!")

    val resultOne = layer.count(1) * layer.count(2)
    println("part one: $resultOne")

    // part two
    val resultTwo = "TODO"

    println("part two: $resultTwo")
}

private fun List<Int>.count(number: Int) = this.count { it == number }

