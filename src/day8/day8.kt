package day8

import java.io.File
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day8", "day8_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 323
        println("Stage 2 answer is ${stageTwo(input)}") // 1077
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Int {
    val antennas = input.parseInput()
    val resonatedSpots = mutableSetOf<Pair<Int, Int>>()
    antennas.forEach {
        it.value.getPairs().forEach {
            resonatedSpots.addAll(it.getResonatingSpotsPartOne())
        }
    }
    return resonatedSpots.filter { it.isWithingBounds(input) }.count()
}

fun stageTwo(input: List<String>): Int {
    val antennas = input.parseInput()
    val resonatedSpots = mutableSetOf<Pair<Int, Int>>()
    antennas.forEach {
        it.value.getPairs().forEach {
            resonatedSpots.addAll(it.getResonatingSpotsPartTwo())
        }
    }
    return resonatedSpots.count { it.isWithingBounds(input) }
}

private fun Pair<Int, Int>.isWithingBounds(input: List<String>) =
    this.first in (0 until input.first().length) && this.second in input.indices

private fun List<String>.parseInput(): Map<Char, List<Pair<Int, Int>>> {
    val temp = mutableListOf<Antenna>()
    this.forEachIndexed { yIndex, row ->
        row.forEachIndexed { xIndex, element ->
            if (element != '.' && element != '#') {
                temp.add(element to (xIndex to yIndex))
            }
        }
    }

    return temp.groupBy { it.first }.mapValues { it.value.map { it.second } }
}

private fun List<Pair<Int, Int>>.getPairs(): Set<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val result = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    this.forEachIndexed { index, pair ->
        this.subList(index + 1, this.size).forEach {
            result.add(pair to it)
        }
    }
    return result
}

private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.getResonatingSpotsPartOne(): List<Pair<Int, Int>> {
    val (firstAntenna, secondAntenna) = this.toList().sortedBy { it.first }

    val differenceX = (firstAntenna.first - secondAntenna.first).absoluteValue
    val differenceY = (firstAntenna.second - secondAntenna.second).absoluteValue

    return if (firstAntenna.second < secondAntenna.second) {
        listOf(
            ((firstAntenna.first - differenceX) to firstAntenna.second - differenceY),
            ((secondAntenna.first + differenceX) to secondAntenna.second + differenceY)
        )
    } else {
        listOf(
            ((firstAntenna.first - differenceX) to firstAntenna.second + differenceY),
            ((secondAntenna.first + differenceX) to secondAntenna.second - differenceY)
        )
    }
}

// compute 50 points in each direction adding 2 starting, it will be filtered out later to eliminate out of grid points
private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.getResonatingSpotsPartTwo(): List<Pair<Int, Int>> {
    val (firstAntenna, secondAntenna) = this.toList().sortedBy { it.first }

    val differenceX = (firstAntenna.first - secondAntenna.first).absoluteValue
    val differenceY = (firstAntenna.second - secondAntenna.second).absoluteValue

    val result = mutableListOf(this.first, this.second) // first two points will be on antennas

    if (firstAntenna.second < secondAntenna.second) {
        (1..50).forEach {
            result.add((firstAntenna.first - differenceX * it) to (firstAntenna.second - differenceY * it))
            result.add((secondAntenna.first + differenceX * it) to (secondAntenna.second + differenceY * it))
        }
    } else {
        (1..50).forEach {
            result.add(((firstAntenna.first - differenceX * it) to firstAntenna.second + differenceY * it))
            result.add(((secondAntenna.first + differenceX * it) to secondAntenna.second - differenceY * it))
        }
    }

    return result
}

// debug
private fun print(input: List<String>, spots: Set<Pair<Int, Int>>) {
    input.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, _ ->
            if (spots.contains<Pair<Int, Int>>(Pair(columnIndex, rowIndex))) {
                print("#")
            } else {
                print(".")
            }
        }
        print("\n")
    }
}

typealias Antenna = Pair<Char, Pair<Int, Int>>