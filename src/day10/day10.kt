package day10

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day10", "day10_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 646
        println("Stage 2 answer is ${stageTwo(input)}") // 1494
    }
    println("Both stages done in $time")
}

private val DIRECTIONS = listOf(
    0 to -1, // up
    1 to 0, // right
    0 to 1, // left
    -1 to 0 // right
)

fun stageOne(input: List<String>): Int {
    val parsed: List<List<Int>> = input.parse()

    val zeros: Set<Pair<Int, Int>> = parsed.findZeros()

    val deque = ArrayDeque<Pair<Pair<Int, Int>, List<Pair<Int, Int>>>>()

    deque.addAll(zeros.map { it to emptyList() })

    val result = mutableSetOf<Pair<Pair<Int, Int>,Pair<Int, Int>>>()

    while (deque.isNotEmpty()) {
        val first = deque.removeFirst()

        val valueOfFirst = parsed[first.first.first][first.first.second]

        if (valueOfFirst == 9) {
            // reached end
            result.add(first.second.first() to first.first)
            continue
        }

        DIRECTIONS.forEach {
            val move = first.first.add(it)
            try {
                if (parsed[move.first][move.second] == valueOfFirst + 1) {
                    val history = first.second.toMutableList()
                    history.add(first.first)
                    deque.add(move to history)
                }
            } catch (e: IndexOutOfBoundsException) {
                //
            }
        }

    }

    return result.size
}

fun stageTwo(input: List<String>): Int {
    val parsed: List<List<Int>> = input.parse()

    val zeros: Set<Pair<Int, Int>> = parsed.findZeros()

    val deque = ArrayDeque<Pair<Pair<Int, Int>, List<Pair<Int, Int>>>>()

    deque.addAll(zeros.map { it to emptyList() })

    val result = mutableListOf<Pair<Int, Int>>()

    while (deque.isNotEmpty()) {
        val first = deque.removeFirst()

        val valueOfFirst = parsed[first.first.first][first.first.second]

        if (valueOfFirst == 9) {
            // reached end
            result.add(first.second.first())
            continue
        }

        DIRECTIONS.forEach {
            val move = first.first.add(it)
            try {
                if (parsed[move.first][move.second] == valueOfFirst + 1) {
                    val history = first.second.toMutableList()
                    history.add(first.first)
                    deque.add(move to history)
                }
            } catch (e: IndexOutOfBoundsException) {
                //
            }
        }

    }

    return result.size
}

private fun Pair<Int, Int>.add(another: Pair<Int, Int>) = this.first + another.first to this.second + another.second

private fun List<String>.parse(): List<List<Int>> {
    return this.map { it.toCharArray().map {
        if(it == '.') -1 else it.digitToInt()
    } }
}

private fun List<List<Int>>.findZeros(): Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()
    this.forEachIndexed { yIndex, ints ->
        ints.forEachIndexed { xIndex, i ->
            if (i == 0) result.add(Pair(yIndex, xIndex))
        }
    }
    return result
}