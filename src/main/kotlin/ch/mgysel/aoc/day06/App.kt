package ch.mgysel.aoc.day06

import ch.mgysel.aoc.common.InputData

fun main() {
    val data = InputData.readLines("day06-input.txt")
            .map(::parseLine)
    println("Parsed ${data.size} relationships")

    // part one
    val orbitMap = OrbitMap(data)
    val resultOne = orbitMap.countAlOrbits()
    println("part one: $resultOne")

    // part two
    val resultTwo = "TODO"
    println("part two: $resultTwo")
}

fun parseLine(line: String): Relationship {
    val (center, orbiter) = line.split(")")
    return Relationship(center, orbiter)
}

class OrbitMap(relationships: List<Relationship>) {

    private val root: Object
    private val distanceToCOM: Map<String, Int>

    init {
        val distances = mutableMapOf<String, Int>()
        root = parse(relationships, "COM", 0, distances)
        distanceToCOM = distances
    }

    private fun parse(relationships: List<Relationship>, name: String, currentDistance: Int, distances: MutableMap<String, Int>): Object {
        val orbiters = relationships
                .filter { relationship ->
                    relationship.center == name
                }
                .map { relationship ->
                    parse(relationships, relationship.orbiter, currentDistance + 1, distances)
                }
        distances[name] = currentDistance
        return Object(name, orbiters)
    }

    fun countOrbits(name: String) = distanceToCOM[name]
    fun countAlOrbits() = distanceToCOM.values.sum()

}

class Object(name: String,
             orbiters: List<Object>)

data class Relationship(val center: String,
                        val orbiter: String)