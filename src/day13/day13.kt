package day13

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day13", "day13_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") //
        println("Stage 2 answer is ${stageTwo(input)}") //
    }
    println("Both stages done in $time")
}

private const val A_BUTTON_COST = 3
private const val B_BUTTON_COST = 1

fun stageOne(input: List<String>): Long {
    return input.parse(false)
        .asSequence()
        .filter { it.wyznacznik != 0L }
        .filter { it.wyznacznikA % it.wyznacznik == 0L && it.wyznacznikB % it.wyznacznik == 0L }
        .map { it.wyznacznikA / it.wyznacznik to it.wyznacznikB / it.wyznacznik }
        .filter { it.first in (0..100) && it.second in (0..100) }
        .sumOf { it.first * A_BUTTON_COST + it.second * B_BUTTON_COST }
}

fun stageTwo(input: List<String>): Long {
    return input.parse(true)
        .filter { it.wyznacznik != 0L }
        .filter { it.wyznacznikA % it.wyznacznik == 0L && it.wyznacznikB % it.wyznacznik == 0L }
        .map { it.wyznacznikA / it.wyznacznik to it.wyznacznikB / it.wyznacznik }
        .sumOf { it.first * A_BUTTON_COST + it.second * B_BUTTON_COST }
}

private fun List<String>.parse(add: Boolean): List<ClawMachine> {
    return this.filter { it.isNotBlank() }.chunked(3).map {
        ClawMachine(
            it[0].getFromLine(),
            it[1].getFromLine(),
            it[2].getFromLine(add)
        )
    }
}

private fun String.getFromLine(add: Boolean = false): Pair<Long, Long> {
    val numbers = "\\d+".toRegex().findAll(this).toList().map { it.value.toLong() }
    var adder = 0L
    if(add) {
        adder = 10000000000000L
    }
    return numbers.first() + adder to numbers.last() + adder
}

data class ClawMachine(
    val buttonA: Pair<Long, Long>,
    val buttonB: Pair<Long, Long>,
    val price: Pair<Long, Long>
) {
    val wyznacznik = (buttonA.first * buttonB.second - buttonA.second * buttonB.first)

    val wyznacznikA = price.first * buttonB.second - price.second * buttonB.first

    val wyznacznikB = buttonA.first * price.second - buttonA.second * price.first
}