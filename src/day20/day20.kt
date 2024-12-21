package day20

import utils.TwoDimensionalGridParser
import kotlin.math.absoluteValue

const val isLoggerEnabled = true
private val ANSWERS = mapOf<String, Long>(
    "TEST1_PART1" to 0, // test input is different condition
    "TEST1_PART2" to 0, // test input is different condition
)


private val LEGAL_CACHE = mutableMapOf<Pair<Int, Int>, Int>()

fun main() {
    Runner.run("20", ANSWERS, { stageOne(it) }, { stageTwo(it) })
}

fun stageOne(input: List<String>): Long {
    LEGAL_CACHE.clear()
    val (walls, start, end) = parseInput(input)
    val legalTime = findLegalWays(walls, start, end)
    val cheatedTimes = findCheatedWays(walls, legalTime, CHEATED_MOVES_PART_1)
    return cheatedTimes.count { it <= legalTime - 100 }.toLong()
}

fun stageTwo(input: List<String>): Long {
    LEGAL_CACHE.clear()
    val (walls, start, end) = parseInput(input)
    val legalTime = findLegalWays(walls, start, end)
    val cheatedTimes = findCheatedWays(walls, legalTime, CHEATED_MOVES_PART_2())
    return cheatedTimes.map { legalTime - it }.filter { it >= 100 }.groupingBy { it }.eachCount().values.sum().toLong()
}

private fun findLegalWays(
    walls: List<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>
): Int {
    val visited = mutableSetOf<Pair<Int, Int>>()

    val stack = ArrayDeque(listOf(State(end, 0)))

    val result = mutableListOf<Int>()

    while (stack.isNotEmpty()) {
        val last = stack.removeFirst()

        if (last.position in walls) {
            continue
        }

        if ((LEGAL_CACHE[last.position] ?: Int.MAX_VALUE) > last.cost) {
            LEGAL_CACHE[last.position] = last.cost
        }

        if (last.position == start) {
            result.add(last.cost)
            continue
        }
        if (last.position in visited) {
            continue
        }
        visited.add(last.position)
        LEGAL_MOVES.forEach { direction ->
            stack.addFirst(
                State(last.position.add(direction), last.cost + 1)
            )
        }
    }
    return result.minOrNull()!!
}

private fun findCheatedWays(
    walls: List<Pair<Int, Int>>, legalWay: Int, cheatedWays: List<Pair<Int, Int>>
): List<Int> {
    val result = mutableListOf<CheatedWay>()

    LEGAL_CACHE.keys.forEach {
        cheatedWays.forEach { cheatDirection ->
            val newPosition = it.add(cheatDirection)
            if (newPosition in walls || newPosition.isOutside(walls)) {
                //
            } else {
                result.add(
                    CheatedWay(
                        it,
                        newPosition,
                        legalWay - LEGAL_CACHE[it]!! + LEGAL_CACHE[newPosition]!! + cheatDirection.first.absoluteValue + cheatDirection.second.absoluteValue // cheated distance
                    )
                )
            }
        }
    }


    return result.groupBy { it.start to it.end }.values.map { it.sortedBy { it.path }.first().path }
}

private data class CheatedWay(
    val start: Pair<Int, Int>, val end: Pair<Int, Int>, val path: Int
)

private fun Pair<Int, Int>.isOutside(walls: List<Pair<Int, Int>>): Boolean {
    val minX = walls.minOf { it.first }
    val maxX = walls.maxOf { it.first }
    val minY = walls.minOf { it.second }
    val maxY = walls.maxOf { it.second }

    return this.first !in minX..maxX || this.second !in minY..maxY

}

private data class State(
    val position: Pair<Int, Int>, val cost: Int
)

private fun parseInput(input: List<String>): Triple<List<Pair<Int, Int>>, Pair<Int, Int>, Pair<Int, Int>> {
    val grid = TwoDimensionalGridParser.parse(input = input, wallsChar = '#', specificChars = listOf('S', 'E'))
    return Triple(grid.walls, grid.specialCharsFound['S']!!.first(), grid.specialCharsFound['E']!!.first())
}

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


private val LEGAL_MOVES = listOf(
    0 to -1, // '^'
    0 to 1, // 'v'
    -1 to 0, // '<'
    1 to 0 // '>'
)

private val CHEATED_MOVES_PART_1 = listOf(
    1 to 1, 1 to -1, -1 to 1, -1 to -1, -2 to 0, 2 to 0, 0 to 2, 0 to -2
)

fun CHEATED_MOVES_PART_2() :List<Pair<Int,Int>> {
    val max = 20
    val result = mutableListOf<Pair<Int,Int>>()
    for (x in -max..max) {
        for (y in -max..max) {
            if(x.absoluteValue + y.absoluteValue <= max) {
                result.add(x to y)
            }
        }
    }
    result.remove(0 to 0)
    return result
}