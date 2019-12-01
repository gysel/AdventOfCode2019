package ch.mgysel.aoc.common

import java.lang.IllegalStateException

class InputData {
    companion object {
        fun read(filename: String): List<String> {
            return this::class.java.classLoader.getResourceAsStream(filename)
                    ?.bufferedReader()
                    ?.readLines()
                    ?: throw IllegalStateException("Unable to load $filename!")
        }
    }
}