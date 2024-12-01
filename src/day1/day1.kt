package day1

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val input = File("src/day1", "day1_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 1938424
    println("Stage 2 answer is ${stageTwo(input)}") // 22014209
}

fun stageOne(input: List<String>): Int {
    val lists = input.computeInput(true)

    var result = 0

    for (i in lists.first.indices) {
        result += (lists.first[i] - lists.second[i]).absoluteValue
    }

    return result
}

fun stageTwo(input: List<String>): Int {
    val lists = input.computeInput(true)
    val rightListGrouped = lists.second.groupBy { it }.mapValues { it.value.size }

    var result = 0

    lists.first.forEach {
        result += (rightListGrouped[it] ?: 0) * it
    }

    return result
}

private fun List<String>.computeInput(sorted: Boolean): Pair<List<Int>, List<Int>> {
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()
    this.forEach {
        val split = it.split("   ")
        leftList.add(split[0].toInt())
        rightList.add(split[1].toInt())
    }

    takeIf { sorted }
        ?.let {
            leftList.sort()
            rightList.sort()
        }

    return leftList to rightList
}