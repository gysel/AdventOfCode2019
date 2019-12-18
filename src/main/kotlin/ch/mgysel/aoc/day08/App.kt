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
    verify(resultOne, 1862, "part one")

    // part two
    val merged = mergeLayers(layers)

    println("part two:")
    printImage(merged, width)
}

private fun printImage(merged: List<Int>, width: Int) {
    println()
    merged.chunked(width)
            .map { line -> line.joinToString("") { if (it == 0) " " else "X" } }
            .forEach(::println)
    println()
}

fun mergeLayers(layers: List<List<Int>>): List<Int> {
    return (layers.first().indices).map { i ->
        layers.map { it[i] }.first { it != 2 }
    }
}

private fun List<Int>.count(number: Int) = this.count { it == number }

