package ch.mgysel.aoc.day14

import ch.mgysel.aoc.common.InputData
import kotlin.math.ceil

fun main() {
    val reactions = loadReactions()

    // part one
    val resultOne = produce("1 FUEL".toChemical(), "ORE", reactions)
    println("part one: $resultOne")

    // part two
    val resultTwo = "TODO"
    println("part two: $resultTwo")
}

class Result {

    private val leftovers = mutableMapOf<String, Int>()
    var usedOre = 0

    fun addLeftovers(chemicalAmount: ChemicalAmount) {
        updateAmount(chemicalAmount.chemical) { it + chemicalAmount.amount }
    }

    fun useLeftovers(chemicalAmount: ChemicalAmount): Int {
        val leftoverAmount = leftovers.getOrDefault(chemicalAmount.chemical, 0)
        return when {
            leftoverAmount < chemicalAmount.amount -> leftoverAmount
            else -> chemicalAmount.amount
        }.also { usedAmount ->
            updateAmount(chemicalAmount.chemical) { oldValue ->
                oldValue - usedAmount
            }
        }
    }

    private fun updateAmount(chemical: String, recalculate: (Int) -> Int) {
        leftovers.compute(chemical) { _, oldValue ->
            recalculate(oldValue ?: 0)
        }
    }

}

fun produce(target: ChemicalAmount, withChemical: String, reactions: List<Reaction>): Int {
    val catalog = reactions.associateBy { it.output.chemical }
    val result = Result()
    produce(target, withChemical, catalog, result)
    return result.usedOre
}


fun produce(target: ChemicalAmount, withChemical: String, catalog: Map<String, Reaction>, result: Result) {
    if (target.chemical == withChemical) {
        result.usedOre += target.amount
    } else {
        catalog[target.chemical]?.let { reaction ->
            val toProduce = target - result.useLeftovers(target)
            val multiplier = ceil(toProduce.amount.toDouble() / reaction.output.amount).toInt()
            val produced = reaction.output * multiplier
            reaction.inputs.forEach { input ->
                produce(input * multiplier, withChemical, catalog, result)
            }
            val leftOvers = produced - toProduce
            result.addLeftovers(leftOvers)
        } ?: throw IllegalStateException("No reaction found for ${target.chemical}")
    }
}

data class Reaction(val inputs: List<ChemicalAmount>,
                    val output: ChemicalAmount) {
    override fun toString(): String {
        return "$inputs => $output"
    }

    companion object {
        fun parse(output: String, vararg inputs: String): Reaction {
            return Reaction(inputs.map { it.toChemical() }, output.toChemical())
        }
    }
}

data class ChemicalAmount(val chemical: String,
                          val amount: Int) {

    override fun toString(): String = "$amount $chemical"

    operator fun minus(other: ChemicalAmount): ChemicalAmount {
        if (chemical != other.chemical) throw IllegalStateException("Chemicals must be equal!")
        return ChemicalAmount(chemical, this.amount - other.amount)
    }

    operator fun times(multiplier: Int): ChemicalAmount {
        return ChemicalAmount(chemical, amount * multiplier)
    }

    operator fun minus(other: Int): ChemicalAmount {
        return ChemicalAmount(chemical, amount - other)
    }
}

fun String.toChemical(): ChemicalAmount {
    val (amount, chemical) = this.trim()
            .split(" ")
            .map(String::trim)
    return ChemicalAmount(chemical, amount.toInt())
}

fun parseReaction(line: String): Reaction {
    val (input, output) = line.split("=>").map(String::trim)
    val inputs: List<ChemicalAmount> = input
            .split(",")
            .map(String::toChemical)
    return Reaction(inputs, output.toChemical())
}

fun parseInput() = InputData.readLines("day14-input.txt")

fun loadReactions() = parseInput().map(::parseReaction)
