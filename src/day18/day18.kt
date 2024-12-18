package day18

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

const val isLoggerEnabled = true

private val DIRECTION = listOf(
    (0 to 1),
    (1 to 0),
    (0 to -1),
    (-1 to 0)
)

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day18", "day18_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}")
        println("Stage 2 answer is ${stageTwo(input)}")
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Long {
    val obstacles = input.parseInput().take(1024)
    val boundary = 70
    val xRange = (0..boundary)
    val yRange = (0..boundary)
    val start = 0 to 0
    val end = boundary to boundary

    val stack = ArrayDeque(listOf(State(start, 0)))

    var top = Int.MAX_VALUE

    val seen = mutableListOf<Pair<Int, Int>>()

    while (stack.isNotEmpty()) {
        val last = stack.removeFirst()

        if (seen.contains(last.position)) {
            continue
        }

        if (obstacles.contains(last.position)) {
            // in obstacle
            continue
        }

        if (last.position.first !in xRange || last.position.second !in yRange) {
            // out of boundary
            continue
        }


        if (last.position == end) {
            if (top > last.steps) {
                top = last.steps
            }
        }

        seen.add(last.position)

        DIRECTION.forEach { direction ->
            val newPosition = last.position.add(direction)
            stack.add(State(newPosition, last.steps + 1))
        }

    }


    return top.toLong()
}

fun stageTwo(input: List<String>): String {
    var firstCandidate = 1025
    while(isEndReachable(firstCandidate, input)) {
        firstCandidate++
    }

    return input[firstCandidate - 1]
}

private fun isEndReachable(firstBytes: Int, input: List<String>): Boolean {
    val obstacles = input.parseInput().take(firstBytes)
    val boundary = 70
    val xRange = (0..boundary)
    val yRange = (0..boundary)
    val start = 0 to 0
    val end = boundary to boundary

    val stack = ArrayDeque(listOf(State(start, 0)))

    val seen = mutableListOf<Pair<Int, Int>>()

    while (stack.isNotEmpty()) {
        val last = stack.removeFirst()

        if (seen.contains(last.position)) {
            continue
        }

        if (obstacles.contains(last.position)) {
            // in obstacle
            continue
        }

        if (last.position.first !in xRange || last.position.second !in yRange) {
            // out of boundary
            continue
        }


        if (last.position == end) {
            return true
        }

        seen.add(last.position)

        DIRECTION.forEach { direction ->
            val newPosition = last.position.add(direction)
            stack.add(State(newPosition, last.steps + 1))
        }

    }
    return false
}

private data class State(
    val position: Pair<Int, Int>,
    val steps: Int = 0
)

private fun List<String>.parseInput() =
    this.map { it.split(",") }.map { it[0].toInt() to it[1].toInt() }

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

private fun Pair<Int, Int>.add(another: Pair<Int, Int>) = this.first + another.first to this.second + another.second