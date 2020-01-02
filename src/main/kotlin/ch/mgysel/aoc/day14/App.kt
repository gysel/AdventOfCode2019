package ch.mgysel.aoc.day14

import ch.mgysel.aoc.common.InputData
import kotlin.math.ceil
import kotlin.math.pow

fun main() {
    val reactions = loadReactions()
    val catalog = createReactionCatalog(reactions)

    // part one
    val resultOne = produce("1 FUEL".toChemical(), "ORE", catalog)
    println("part one: $resultOne")

    // part two
    val resultTwo = solvePartTwo(catalog)
    println("part two: $resultTwo")
}

fun solvePartTwo(catalog: Map<String, Reaction>): Int {
    val oreAvailable = 10.0.pow(12).toLong()
    var fuelCounter = 0
    val result = Result()
    do {
        fuelCounter++
        // idea: start with bigger steps, then use smaller steps as we approach the limit
        val step = ChemicalAmount("FUEL", 1)
        produce(step, "ORE", catalog, result)
        // println("$fuelCounter FUEL requires ${result.countUsedOre()} ORE")
    } while (result.countUsedOre() < oreAvailable)
    return fuelCounter - 1
}

class Result {

    private val leftovers = mutableMapOf<String, Int>()
    private var usedOre = 0L

    fun addUsedOre(amount: Int) {
        usedOre += amount
    }

    fun countUsedOre() = usedOre

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

fun produce(target: ChemicalAmount, withChemical: String, catalog: Map<String, Reaction>): Long {
    val result = Result()
    produce(target, withChemical, catalog, result)
    return result.countUsedOre()
}


fun produce(target: ChemicalAmount, withChemical: String, catalog: Map<String, Reaction>, result: Result) {
    if (target.chemical == withChemical) {
        result.addUsedOre(target.amount)
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

fun createReactionCatalog(reactions: List<Reaction>) =
        reactions.associateBy { it.output.chemical }
