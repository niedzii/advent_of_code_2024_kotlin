package day12

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day12", "day12_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 1370258
        println("Stage 2 answer is ${stageTwo(input)}") // 805814
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Int {
    return input.parseInput().sumOf { it.area * it.perimeter() }
}

fun stageTwo(input: List<String>): Int {
    return input.parseInput().sumOf { it.area * it.sides() }
}

private fun List<String>.parseInput(): List<Plot> {
    val result = mutableListOf<Plot>()

    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { cellIndex, cell ->
            if (result.flatMap { it.cells }.contains(cellIndex to rowIndex)) {
                // this cell was already counted
            } else {
                result.add(getPlot(cellIndex to rowIndex, this))
            }
        }
    }

    return result
}

private fun getPlot(start: Pair<Int, Int>, input: List<String>): Plot {
    val fruit = input.get(start)!!
    val stack = ArrayDeque(listOf(start))

    val result = mutableListOf<Pair<Int, Int>>()

    while (stack.isNotEmpty()) {
        val last = stack.removeFirst()

        if (input.get(last) == fruit && !result.contains(last)) {
            result.add(last)
            DIRECTIONS.forEach { direction ->
                stack.add((last.first to last.second).add(direction))
            }
        }
    }

    return Plot(fruit, result)
}

private data class Plot(
    val fruit: Char,
    val cells: MutableList<Pair<Int, Int>> = mutableListOf()
) {

    val area: Int = cells.size

    fun perimeter(): Int {
        var result = 0
        cells.forEach { cell ->
            DIRECTIONS.forEach {
                if (!cells.contains(cell.add(it))) {
                    result += 1
                }
            }
        }
        return result
    }

    fun sides(): Int {
        val minX = cells.minOf { it.first }
        val maxX = cells.maxOf { it.first }
        val minY = cells.minOf { it.second }
        val maxY = cells.maxOf { it.second }

        var horizontalResult = 0
        var verticalResult = 0
        // left and right
        (minX..maxX).forEach { x ->
            var isLeftFenceOngoing = false
            var isRightFenceOngoing = false
            (minY..maxY).forEach { y ->
                val current = x to y

                if (cells.contains(current)) {
                    // leftSide
                    if (!cells.contains(current.add(LEFT))) {
                        // there is edge
                        if (!isLeftFenceOngoing) {
                            verticalResult += 1
                        }
                        isLeftFenceOngoing = true
                    } else {
                        isLeftFenceOngoing = false
                    }

                    // rightSide
                    if (!cells.contains(current.add(RIGHT))) {
                        // there is edge
                        if (!isRightFenceOngoing) {
                            verticalResult += 1
                        }
                        isRightFenceOngoing = true
                    } else {
                        isRightFenceOngoing = false
                    }
                } else {
                    isLeftFenceOngoing = false
                    isRightFenceOngoing = false
                }

                if (y == maxY) {
                    isLeftFenceOngoing = false
                    isRightFenceOngoing = false
                }
            }
        }


        // up and down
        (minY..maxY).forEach { y ->
            var isUpFenceOngoing = false
            var isDownFenceOngoing = false
            (minX..maxX).forEach { x ->
                val current = x to y
                if (cells.contains(current)) {
                    // upSide
                    if (!cells.contains(current.add(UP))) {
                        // there is edge
                        if (!isUpFenceOngoing) {
                            horizontalResult += 1
                        }
                        isUpFenceOngoing = true
                    } else {
                        isUpFenceOngoing = false
                    }

                    // downSide
                    if (!cells.contains(current.add(DOWN))) {
                        // there is edge
                        if (!isDownFenceOngoing) {
                            horizontalResult += 1
                        }
                        isDownFenceOngoing = true
                    } else {
                        isDownFenceOngoing = false
                    }
                } else {
                    isUpFenceOngoing = false
                    isDownFenceOngoing = false
                }
                if (x == maxX) {
                    isUpFenceOngoing = false
                    isDownFenceOngoing = false
                }
            }

        }

        return horizontalResult + verticalResult
    }
}

private fun Pair<Int, Int>.add(another: Pair<Int, Int>) = this.first + another.first to this.second + another.second

private fun List<String>.get(coords: Pair<Int, Int>): Char? {
    return try {
        this[coords.second][coords.first]
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}

private val RIGHT = 1 to 0
private val LEFT = -1 to 0
private val UP = 0 to -1
private val DOWN = 0 to 1

private val DIRECTIONS = listOf(
    RIGHT,
    LEFT,
    UP,
    DOWN
)