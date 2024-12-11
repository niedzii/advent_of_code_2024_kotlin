package day11

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day11", "day11_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") //
        println("Stage 2 answer is ${stageTwo(input)}") //
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Long {
   return calculateStones(25, input)
}

fun stageTwo(input: List<String>): Long {
    return calculateStones(75, input)
}

private fun calculateStones(blinks: Int, input: List<String>): Long {
    var stones = input.parseInput().groupBy { it }.map { it.key to it.value.size.toLong() }.toMap()
    (0 until blinks).forEach { _ ->
        val newStonesAfterBlink = mutableMapOf<Long, Long>()
        stones.forEach { stone ->
            val newStones = calculateBlink(stone.key)
            newStones.forEach { newStone ->
                newStonesAfterBlink.putIfAbsent(newStone, 0L)
                newStonesAfterBlink[newStone] = newStonesAfterBlink[newStone]!! + stone.value
                stones = newStonesAfterBlink
            }
        }
    }

    return stones.values.sum()
}

private fun calculateBlink(stone: Long) : List<Long> {
    if(stone == 0L) {
        return listOf(1L)
    }

    val asString = stone.toString()
    if(asString.length % 2 == 0) {
        return listOf(asString.substring(0, asString.length / 2).toLong(), asString.substring(asString.length / 2, asString.length).toLong())
    }

    return listOf(stone * 2024L)
}

private fun List<String>.parseInput() = this.first().split(" ").map { it.toLong() }