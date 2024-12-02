package day2

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val input = File("src/day2", "day2_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 252
    println("Stage 2 answer is ${stageTwo(input)}") // 324
}

fun stageOne(input: List<String>): Int {
    val xd = input
        .map { it.split(" ") }
        .map { it.map { it.toInt() } }
        .filter {
            it.zipWithNext().isReportCorrect()
        }

    return xd.count()
}

fun stageTwo(input: List<String>): Int {
    val (correctReports, incorrectReports) = input
        .map { it.split(" ") }
        .map { it.map { it.toInt() } }
        .partition  {
            it.zipWithNext().isReportCorrect()
        }

    var correctNumber = correctReports.size

    incorrectReports.forEach { row ->
        for ((index, i) in row.withIndex()) {
            val newRow = row.toMutableList()
            newRow.removeAt(index)
            if (newRow.zipWithNext().isReportCorrect()) {
                correctNumber += 1
                break
            }
        }
    }
    return correctNumber
}

private fun List<Pair<Int, Int>>.isReportCorrect() =
    (this.all { (it.first < it.second) } || this.all { (it.first > it.second) }) && this.all { (it.first - it.second in -3..3) }
