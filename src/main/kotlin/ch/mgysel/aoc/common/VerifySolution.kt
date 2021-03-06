package ch.mgysel.aoc.common

fun verify(actual: Int, expected: Int, part: String) {
    if (actual != expected)
        throw IllegalStateException("$part is wrong. Expected $expected but was $actual")
}