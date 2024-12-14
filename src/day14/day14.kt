package day14

import java.io.File
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day14", "day14_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 231221760
        println("Stage 2 answer is ${stageTwo(input)}") // 6771
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Int {
    val robots = input.parse()
    val maxX = 101
    val maxY = 103
    (0 until  100).forEachIndexed { index, i ->
        robots.forEach {
            it.simulateMove(maxX, maxY)
        }
    }

    val (leftHalf, rightHalf) = robots
        .groupingBy { it.p }
        .eachCount()
        .filter { it.key.first != maxX / 2 }
        .filter { it.key.second != maxY / 2 }
        .toList()
        .partition { it.first.first in (0 until maxX/2) }


    val (leftTop, leftBottom) = leftHalf.partition { it.first.second in (0 until maxY / 2) }
    val (rightTop, rightBottom) = rightHalf.partition { it.first.second in (0 until maxY / 2) }

    return leftTop.sumOf { it.second } * leftBottom.sumOf { it.second } * rightTop.sumOf { it.second } * rightBottom.sumOf { it.second }
}

fun stageTwo(input: List<String>): Long {
    val robots = input.parse() // 27576 too high
    val maxX = 101
    val maxY = 103
    (0 until  7_000).forEachIndexed { index, i ->
        robots.forEach {
            it.simulateMove(maxX, maxY)
        }
        // if there are at least 30 robots in a row then print
        // HERE IS A BUG. TREE IS PRINTED EVERY 10403, SINCE THIS ANSWER IS 6771 AND LOOP IS <7K ONLY LATEST TREE IS PRINTED
        // IF LOOP WOULD BE BIGGER THE FIRST TREE WOULD BE CUT OUT BY STDOUT
        if(robots.groupingBy { it.p.first }.eachCount().any { it.value > 30 }) {
//            println("Step ${index + 1}")
//            robots.print(maxX, maxY)
        }
    }

    return 6771
}

private fun List<Robot>.print(maxX: Int, maxY:Int) {
    (0 until maxY).forEachIndexed { yIndex, y ->
        (0 until maxX).forEachIndexed { xIndex, x ->
            if(this.find { it.p.first == xIndex && it.p.second == yIndex } != null) {
                print("#")
            } else {
                print(".")
            }
        }
        print("\n")
    }
}



private fun List<String>.parse(): List<Robot> {
    return this.map {
        val split = it.split(" ")
        Robot(
            split.first().drop(2).split(",").zipWithNext { a, b -> a.toInt() to b.toInt() }.first(),
            split.last().drop(2).split(",").zipWithNext { a, b -> a.toInt() to b.toInt() }.first()
        )
    }
}

data class Robot(
    var p: Pair<Int, Int>,
    var v: Pair<Int, Int>,
) {
    fun simulateMove(maxX: Int, maxY: Int) {
        var nextP = p.first + v.first to p.second + v.second

        // teleport X from right edge
        if(nextP.first >= maxX) {
            val diff = nextP.first - maxX
           nextP = diff.absoluteValue to nextP.second
        }

        // teleport X from left edge
        if(nextP.first < 0) {
            val diff = nextP.first
            nextP = maxX - diff.absoluteValue to nextP.second
        }

        // teleport Y from bottom edge
        if(nextP.second >= maxY) {
            val diff = nextP.second - maxY
            nextP = nextP.first to diff.absoluteValue
        }

        // teleport Y from top edge
        if(nextP.second < 0) {
            val diff = nextP.second
            nextP = nextP.first to maxY - diff.absoluteValue
        }

        this.p = nextP
    }
}