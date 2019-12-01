package ch.mgysel.aoc.day01

import ch.mgysel.aoc.common.InputData

fun calculateRequiredFuel(mass: Int): Int {
    return mass / 3 - 2
}

fun main() {
    val data = InputData.read("day01/input.txt")
    // part one
    val sum = data
            .map(String::toInt)
            .map(::calculateRequiredFuel)
            .sum()
    println("part one: $sum")
    // part two
    val solution = data
            .map { it.toInt() }
            .map { initialMass ->
                var result = 0
                var mass = initialMass
                do {
                    mass = calculateRequiredFuel(mass)
                    if (mass > 0) {
                        result += mass
                    }
                } while (mass > 0)
                result
            }
            .sum()
    println("part two: $solution")
}
