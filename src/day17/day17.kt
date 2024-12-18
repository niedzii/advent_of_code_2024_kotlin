package day17

import java.io.File
import kotlin.math.pow
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

const val isLoggerEnabled = false

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day17", "day17_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}")
        println("Stage 2 answer is ${stageTwo(input)}")
    }
    println("Both stages done in $time")
}


fun stageOne(input: List<String>): String {
    val machine = input.parseInput()

    if (machine.instructions.size % 2 != 0) {
        throw IllegalArgumentException("Wrong instruction set")
    }

    while (!machine.isHalted()) {
        machine.performAction()
    }
    return machine.output.joinToString(",")
}

fun stageTwo(input: List<String>): String {
    val stack = ArrayDeque(listOf(State(0L, 0L)))

    val searchedForProgram = input.parseInput().instructions.reversed()
    val result = mutableListOf<State>()

    while (stack.isNotEmpty()) {
        val last = stack.removeLast()

        if (last.iteration.toInt() == searchedForProgram.size) {
            // we found program length so end
            result.add(last)
            continue
        }

        if (last.iteration != 0L && test(input, last.result) != searchedForProgram.take(last.iteration.toInt())
                .reversed()
                .joinToString(",")
        ) {
            // used to truncate not correct branches since there are up to 8 possibilities per step
            continue
        }

        // Currently searched for part of program note that searching is reversed
        val searchedFor = searchedForProgram[last.iteration.toInt()]
        val offset = last.result shl 3

        (offset..(offset + 7)).forEach {
            val calculation = extractedMath(it)
            if (calculation == searchedFor) {
                stack.addLast(State(it, last.iteration + 1))
            }
        }
    }

    return result.minByOrNull { it.result }!!.result.toString()
}


private data class State(
    var result: Long,
    var iteration: Long
)

// This function represents what is going on in single program run as F(A) = what_is_output. Look end of file
private fun extractedMath(a: Long): Long {
    val first = a % 8 xor 1
    val second = (a / 2.0.pow((a % 8 xor 1).toDouble())).toLong()
    return (first xor second xor 4) % 8
}

// Test what is output of machine given an A
private fun test(input: List<String>, a: Long): String {
    val machine = input.parseInput()

    if (machine.instructions.size % 2 != 0) {
        throw IllegalArgumentException("Wrong instruction set")
    }

    machine.regA = a

    while (!machine.isHalted()) {
        machine.performAction()
    }
    return machine.output.joinToString(",")
}

private fun List<String>.parseInput(): Machine {
    return Machine(
        this[0].drop(12).toLong(),
        this[1].drop(12).toLong(),
        this[2].drop(12).toLong(),
        this[4].drop(9).split(",").map { it.toLong() }.toMutableList()
    )
}

private class Machine(
    var regA: Long,
    var regB: Long,
    var regC: Long,
    val instructions: MutableList<Long>,
    var instructionCount: Int = 0,
    val output: MutableList<Long> = mutableListOf(),
) {

    fun isHalted() = instructionCount >= instructions.size

    fun combo(comboOperand: Long): Long {
        return when (comboOperand) {
            0L -> 0L
            1L -> 1L
            2L -> 2L
            3L -> 3L
            4L -> regA
            5L -> regB
            6L -> regC
            else -> {
                throw IllegalArgumentException("Wrong combo operand")
            }
        }
    }

    fun performAction() {
        val (opcode, operand) = instructions[instructionCount] to instructions[instructionCount + 1]

        logLine("Performing $opcode on $operand ")
        if (opcode == 0L) {
            regA = (regA / (2.0.pow(combo(operand).toDouble()))).toLong()
            instructionCount += 2
            return
        }

        if (opcode == 1L) {
            regB = regB xor operand
            instructionCount += 2
            return
        }

        if (opcode == 2L) {
            regB = combo(operand) % 8
            instructionCount += 2
            return
        }

        if (opcode == 3L) {
            if (regA == 0L) {
                instructionCount += 2
                return
            } else {
                instructionCount = operand.toInt()
            }
            return
        }

        if (opcode == 4L) {
            regB = regB xor regC
            instructionCount += 2
            return
        }

        if (opcode == 5L) {
            val value = combo(operand) % 8
            output.add(value)
            instructionCount += 2
            return
        }

        if (opcode == 6L) {
            regB = (regA / (2.0.pow(combo(operand).toDouble()))).toLong()
            instructionCount += 2
            return
        }

        if (opcode == 7L) {
            regC = (regA / (2.0.pow(combo(operand).toDouble()))).toLong()
            instructionCount += 2
            return
        }

        throw IllegalArgumentException("Wrong opcode")
    }
}

private fun log(value: String) {
    if (isLoggerEnabled) {
        print(value)
    }
}

private fun logLine(value: String) {
    if (isLoggerEnabled) {
        println(value)
    }
}


//2,4 -> B = A % 8
//1,1 -> B = B xor 1 -> B = A % 8 xor 1
//7,5 -> C = A / 2^B -> C = A / 2^(A % 8 xor 1)
//4,4 -> B = B xor C -> B = (A % 8 xor 1) xor (A / 2^(A % 8 xor 1))
//1,4 -> B = B xor 4 -> B = (A % 8 xor 1) xor (A / 2^(A % 8 xor 1)) xor 4
//0,3 -> A = A / 2^3 -> A = A / 8
//5,5 -> out(  ((A % 8 xor 1) xor (A / 2^(A % 8 xor 1)) xor 4) % 8      )
//3,0 -> if(A!=0) goto 0 else halt