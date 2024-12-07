package day7

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day7", "day7_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 10741443549536
        println("Stage 2 answer is ${stageTwo(input)}") // 500335179214836
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Long {
    return input.parseInput().filter { isCorrectPartOne(it.first, it.second) }.sumOf { it.first }
}

fun stageTwo(input: List<String>): Long {
    return input.parseInput().filter { isCorrectPartTwo(it.first, it.second) }.sumOf { it.first }
}

private fun List<String>.parseInput(): List<Pair<Long, List<Long>>> {
    return this.map { it.split(": ").let { it[0].toLong() to (it[1].split(" ").map { it.toLong() }) } }
}

private fun isCorrectPartOne(result: Long, input: List<Long>): Boolean {
    return isCorrect(result, input) { wrapper -> wrapper.computePartOne() }
}

private fun isCorrectPartTwo(result: Long, input: List<Long>): Boolean {
    return isCorrect(result, input) { wrapper -> wrapper.computePartTwo() }
}

private fun isCorrect(result: Long, input: List<Long>, map: (Wrapper) -> List<Wrapper>): Boolean {
    val firstWrapped = Wrapper(input.first(), input.drop(1))
    val stack = ArrayDeque(listOf(firstWrapped))
    while (stack.isNotEmpty()) {
        val pop = stack.removeFirst()

        if (pop.currentValue > result) {
            // prune
        } else {
            if (pop.remainingElements.isNotEmpty()) {
                stack.addAll(map(pop))
            } else {
                // reached end
                if (pop.currentValue == result) {
                    return true
                }
            }
        }


    }

    return false
}

data class Wrapper(
    val currentValue: Long,
    val remainingElements: List<Long>
) {

    fun computePartOne(): List<Wrapper> { // in 1 out 2
        if (remainingElements.isEmpty()) {
            return listOf(Wrapper(currentValue, emptyList()))
        }

        return listOf(
            Wrapper(currentValue + remainingElements.first(), remainingElements.drop(1)),
            Wrapper(currentValue * remainingElements.first(), remainingElements.drop(1))
        )
    }

    fun computePartTwo(): List<Wrapper> { // in 1 out 3
        if (remainingElements.isEmpty()) {
            return listOf(Wrapper(currentValue, emptyList()))
        }

        return listOf(
            Wrapper(currentValue + remainingElements.first(), remainingElements.drop(1)),
            Wrapper(currentValue * remainingElements.first(), remainingElements.drop(1)),
            Wrapper(
                (currentValue.toString() + remainingElements.first().toString()).toLong(),
                remainingElements.drop(1)
            )
        )
    }


}
