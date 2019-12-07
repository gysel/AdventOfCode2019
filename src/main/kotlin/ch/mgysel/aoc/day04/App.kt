package ch.mgysel.aoc.day04

fun main() {
    val passwords = (246540..787419).toList()

    val options = passwords.asSequence()
            .filter(::hasAdjacentDigits)
            .filter(::digitsIncrease)
            .toList()

    val resultOne = options.count()
    println("part one: $resultOne")

    // part two
    val resultTwo = options.asSequence()
            .filter(::adjacentDigitsNotPartOfLargerGroup)
            .count()

    println("part two: $resultTwo")
}

fun hasAdjacentDigits(number: Int): Boolean {
    return number.toString().windowed(2, 1)
            .any { it.first() == it.last() }
}

fun digitsIncrease(number: Int): Boolean {
    return number.toString().windowed(2, 1)
            .all { it.first() <= it.last() }
}

fun adjacentDigitsNotPartOfLargerGroup(number: Int): Boolean {
    return calculateGroups(number).any { it.count == 2 }
}

fun calculateGroups(number: Int): List<CharCount> {
    val groups = mutableListOf<CharCount>()
    return number.toString().fold(groups) { v, char ->
        if (v.isEmpty() || v.last().char != char) {
            v.add(CharCount(char, 1))
        } else {
            v.last().count++
        }
        v
    }
}

data class CharCount(val char: Char,
                     var count: Int)

