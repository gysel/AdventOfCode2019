package ch.mgysel.aoc.day06

import ch.mgysel.aoc.common.InputData

fun main() {
    val data = InputData.readLines("day06-input.txt")
            .map(::parseLine)
    println("Parsed ${data.size} relationships")

    // part one
    val orbitMap = OrbitMap(data)
    val resultOne = orbitMap.countAllOrbits()
    println("part one: $resultOne")

    // part two
    val resultTwo = orbitMap.calculateTransfer("YOU", "SAN")
    println("part two: $resultTwo")
}

fun parseLine(line: String): Relationship {
    val (center, orbiter) = line.split(")")
    return Relationship(center, orbiter)
}

class OrbitMap(relationships: List<Relationship>) {

    private val objectsByName: Map<String, SpaceObject>

    init {
        objectsByName = create(null, relationships, "COM", 0)
                .associateBy(SpaceObject::name)
    }

    private fun create(orbiting: SpaceObject?, relationships: List<Relationship>, name: String, currentDistance: Int): Sequence<SpaceObject> {
        val newObject = SpaceObject(name, currentDistance, orbiting)
        return sequenceOf(newObject) + relationships
                .asSequence()
                .filter { relationship ->
                    relationship.center == name
                }
                .flatMap { relationship ->
                    create(newObject, relationships, relationship.orbiter, currentDistance + 1)
                }
    }

    fun countOrbits(name: String) = objectsByName[name]?.distanceToCOM
    fun countAllOrbits() = objectsByName.values.map(SpaceObject::distanceToCOM).sum()
    fun calculateTransfer(from: String, to: String): Int {
        val first = objectsByName[from]?.orbiting ?: throw IllegalStateException("$from is not in an orbit!")
        val second = objectsByName[to]?.orbiting ?: throw IllegalStateException("$to is not in an orbit!")
        val commonPath = findPathFromCom(first).zip(findPathFromCom(second))
                .filter { pair ->
                    pair.first.name == pair.second.name
                }
                .dropLast(1) // the last node of the common path is required for travelling
        return findPathFromCom(first).size + findPathFromCom(second).size - (2 * commonPath.size)
    }

    private fun findPathFromCom(first: SpaceObject): List<SpaceObject> {
        val result = mutableListOf<SpaceObject>()
        var orbiting = first.orbiting
        while (orbiting != null) {
            result.add(orbiting)
            orbiting = orbiting.orbiting
        }
        return result.reversed()
    }

}

data class SpaceObject(val name: String,
                       val distanceToCOM: Int,
                       val orbiting: SpaceObject?)

data class Relationship(val center: String,
                        val orbiter: String)