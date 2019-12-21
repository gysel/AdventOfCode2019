package ch.mgysel.aoc.common

class Program(private val code: List<Int>) {

    private val noInput = { throw IllegalStateException("No input expected") }

    fun run(): List<Int> {
        val result = mutableListOf<Int>()
        run(noInput, { result.add(it) })
        return result
    }

    fun run(input: Int): Int? {
        var result: Int? = null
        run({ input }, { result = it })
        return result
    }

    fun run(readInput: () -> Int, writeOutput: (Int) -> Unit) {
        val memory = code.toMutableList()

        var pointer = 0
        var relativeBase = 0
        while (true) {
            val instruction = parseInstruction(memory[pointer])
            var nextPointer = pointer + instruction.code.length
            val calculatePosition = { index: Int ->
                when (instruction.mode(index)) {
                    Mode.POSITION -> memory[pointer + index]
                    Mode.IMMEDIATE -> pointer + index
                    Mode.RELATIVE -> relativeBase + memory[pointer + index]
                }
            }
            when (instruction.code) {
                OpCode.PLUS -> runInstruction(memory, calculatePosition, Int::plus)
                OpCode.MULTIPLY -> runInstruction(memory, calculatePosition, Int::times)
                OpCode.INPUT -> {
                    val destination = calculatePosition(1)
                    memory[destination] = readInput()
                }
                OpCode.OUTPUT -> {
                    val source = calculatePosition(1)
                    writeOutput(memory[source])
                }
                OpCode.JUMP_IF_TRUE -> {
                    nextPointer = jump(memory, nextPointer, calculatePosition) { it != 0 }
                }
                OpCode.JUMP_IF_FALSE -> {
                    nextPointer = jump(memory, nextPointer, calculatePosition) { it == 0 }
                }
                OpCode.LESS_THAN -> {
                    val lessThan = { a: Int, b: Int ->
                        if (a < b) 1 else 0
                    }
                    runInstruction(memory, calculatePosition, lessThan)
                }
                OpCode.EQUALS -> {
                    val equals = { a: Int, b: Int ->
                        if (a == b) 1 else 0
                    }
                    runInstruction(memory, calculatePosition, equals)
                }
                OpCode.ADJUST_REL_BASE -> {
                    val parameter = calculatePosition(1)
                    relativeBase = memory[parameter]

                }
                OpCode.STOP -> return
            }
            pointer = nextPointer
        }
    }

}

private fun jump(memory: MutableList<Int>,
                 nextPointer: Int,
                 calculatePosition: (Int) -> Int,
                 condition: (Int) -> Boolean): Int {
    val test = calculatePosition(1)
    if (condition(memory[test])) {
        return memory[calculatePosition(2)]
    }
    return nextPointer
}

private fun runInstruction(program: MutableList<Int>,
                           calculatePosition: (Int) -> Int,
                           operation: (Int, Int) -> Int) {
    val first = calculatePosition(1)
    val second = calculatePosition(2)
    val destination = calculatePosition(3)
    program[destination] = operation.invoke(program[first], program[second])
}

fun parseInstruction(instruction: Int): Instruction {
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