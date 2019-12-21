package ch.mgysel.aoc.common

import java.util.*

// TODO get rid of all !!
// TODO add extension function to read from and write to memory

class Program<T : Number>(code: List<T>) {
    private val code: List<Long> = code.map { it.toLong() }

    private val noInput = { throw IllegalStateException("No input expected") }

    fun run(): List<Long> {
        val output = mutableListOf<Long>()
        run(noInput, { output.add(it) })
        return output
    }

    fun run(input: List<T>): List<Long> {
        val result = mutableListOf<Long>()
        val inputQueue = LinkedList(input)
        run(inputQueue::pop) { result.add(it) }
        return result
    }

    fun run(input: T): Long? {
        var result: Long? = null
        run({ input }, { result = it })
        return result
    }

    fun run(readInput: () -> T, writeOutput: (Long) -> Unit) {
        val memory = createMemory()

        var pointer = 0
        var relativeBase = 0
        while (true) {
            val instruction = parseInstruction(memory[pointer]!!)
            var nextPointer = pointer + instruction.code.length
            val calculatePosition: (Int) -> Int = { index ->
                when (instruction.mode(index)) {
                    Mode.POSITION -> memory[pointer + index]!!.toInt()
                    Mode.IMMEDIATE -> pointer + index
                    Mode.RELATIVE -> relativeBase + memory[pointer + index]!!.toInt()
                }
            }
            when (instruction.code) {
                OpCode.PLUS -> runInstruction(memory, calculatePosition, Long::plus)
                OpCode.MULTIPLY -> runInstruction(memory, calculatePosition, Long::times)
                OpCode.INPUT -> {
                    val destination = calculatePosition(1)
                    memory[destination] = readInput().toLong()
                }
                OpCode.OUTPUT -> {
                    val source = calculatePosition(1)
                    writeOutput(memory[source]!!)
                }
                OpCode.JUMP_IF_TRUE -> {
                    nextPointer = jump(memory, nextPointer, calculatePosition) { it != 0L }
                }
                OpCode.JUMP_IF_FALSE -> {
                    nextPointer = jump(memory, nextPointer, calculatePosition) { it == 0L }
                }
                OpCode.LESS_THAN -> {
                    val lessThan = { a: Long, b: Long ->
                        if (a < b) 1L else 0L
                    }
                    runInstruction(memory, calculatePosition, lessThan)
                }
                OpCode.EQUALS -> {
                    val equals = { a: Long, b: Long ->
                        if (a == b) 1L else 0L
                    }
                    runInstruction(memory, calculatePosition, equals)
                }
                OpCode.ADJUST_REL_BASE -> {
                    val parameter = calculatePosition(1)
                    relativeBase += memory[parameter]!!.toInt()

                }
                OpCode.STOP -> return
            }
            pointer = nextPointer
        }
    }

    private fun createMemory() = code
            .mapIndexed { i, v -> i to v }
            .toMap()
            .toMutableMap()

}

private fun jump(memory: MutableMap<Int,Long>,
                 nextPointer: Int,
                 calculatePosition: (Int) -> Int,
                 condition: (Long) -> Boolean): Int {
    val test = calculatePosition(1)
    if (condition(memory[test]!!)) {
        return memory[calculatePosition(2)]!!.toInt()
    }
    return nextPointer
}

private fun runInstruction(program: MutableMap<Int,Long>,
                           calculatePosition: (Int) -> Int,
                           operation: (Long, Long) -> Long) {
    val first = calculatePosition(1)
    val second = calculatePosition(2)
    val destination = calculatePosition(3)
    program[destination] = operation.invoke(program[first] ?: 0, program[second] ?: 0)
}

fun parseInstruction(instruction: Long): Instruction {
    val string = instruction.toString()
    val opCode = OpCode.fromCode(string.takeLast(2).toInt())
    val modes = string.dropLast(2).reversed().map {
        when (it) {
            '0' -> Mode.POSITION
            '1' -> Mode.IMMEDIATE
            '2' -> Mode.RELATIVE
            else -> throw IllegalStateException("Unexpected mode $it")
        }
    }
    return Instruction(opCode, modes)
}

data class Instruction(val code: OpCode,
                       val parameterModes: List<Mode>) {
    fun mode(parameter: Int): Mode {
        return if (parameterModes.size >= parameter)
            parameterModes[parameter - 1]
        else
            Mode.POSITION
    }
}

enum class Mode {
    POSITION, IMMEDIATE, RELATIVE
}

enum class OpCode(val code: Int,
                  val length: Int) {
    PLUS(1, 4),
    MULTIPLY(2, 4),
    INPUT(3, 2),
    OUTPUT(4, 2),
    JUMP_IF_TRUE(5, 3),
    JUMP_IF_FALSE(6, 3),
    LESS_THAN(7, 4),
    EQUALS(8, 4),
    ADJUST_REL_BASE(9, 2),
    STOP(99, 1);

    companion object {
        private val values = values().map { it.code to it }.toMap()
        fun fromCode(code: Int): OpCode = values[code]
                ?: throw IllegalStateException("Operation with code $code not found")
    }
}