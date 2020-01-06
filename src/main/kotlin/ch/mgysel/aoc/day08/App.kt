package ch.mgysel.aoc.day08

import ch.mgysel.aoc.common.InputData
import ch.mgysel.aoc.common.verify

typealias Layer = List<Int>

fun main() {
    // part one
    val pixels = InputData.read("day08-input.txt").map { char -> char.toString().toInt() }
    val width = 25
    val height = 6
    val pixelsPerLayer = width * height
    verify(pixels.size % pixelsPerLayer, 0, "uneven pixel number")
    val layers: List<Layer> = pixels.chunked(pixelsPerLayer)
    println("Found ${layers.size} layers")

    val countZero = { layer: List<Int> -> layer.count(0) }
    val layer = layers.minBy(countZero) ?: throw IllegalStateException("No layer found!")

    val resultOne = layer.count(1) * layer.count(2)
    println("part one: $resultOne")
    verify(resultOne, 1862, "part one")

    // part two
    println("part two:")
    mergeLayers(layers)
            .printImage(width)
}

private fun Layer.printImage(width: Int) {
    println()
    this.chunked(width)
            .map { line -> line.joinToString("") { if (it == 0) " " else "X" } }
            .forEach(::println)
    println()
}

fun mergeLayers(layers: List<Layer>): Layer {
    return (layers.first().indices).map { i ->
        layers.asSequence().map { it[i] }.first { it != 2 }
    }
}

private fun List<Int>.count(number: Int) = this.count { it == number }

