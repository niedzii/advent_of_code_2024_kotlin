package day16

import Runner
import utils.TwoDimensionalGridParser

const val isLoggerEnabled = false
private val ANSWERS = mapOf<String, Long>(
    "TEST1_PART1" to 7036,
    "TEST1_PART2" to 45,
    "TEST2_PART1" to 11048,
    "TEST2_PART2" to 64
)

private val DIRECTION = mapOf(
    '^' to (0 to -1),
    'v' to (0 to 1),
    '<' to (-1 to 0),
    '>' to (1 to 0)
)

private val WAYS = mapOf(
    '^' to listOf('^' to DIRECTION['^']!!, '<' to DIRECTION['<']!!, '>' to DIRECTION['>']!!),
    'v' to listOf('v' to DIRECTION['v']!!, '<' to DIRECTION['<']!!, '>' to DIRECTION['>']!!),
    '<' to listOf('<' to DIRECTION['<']!!, '^' to DIRECTION['^']!!, 'v' to DIRECTION['v']!!),
    '>' to listOf('>' to DIRECTION['>']!!, '^' to DIRECTION['^']!!, 'v' to DIRECTION['v']!!)
)

fun main() {
    Runner.run("16", ANSWERS, { stageOne(it) }, { stageTwo(it) })
}

fun stageOne(input: List<String>): Long {
    return calculate(input).first
}

fun stageTwo(input: List<String>): Long {
    return calculate(input).second
}

private fun calculate(input: List<String>): Pair<Long, Long> {
    val (walls, start, end) = parseInput(input)

    val stack = ArrayDeque<State>()

    stack.add(State('>', start, mutableListOf(), 0))

    var top = Long.MAX_VALUE
    val bestPaths = mutableSetOf<State>()

    var index = 0

    val seen = mutableMapOf<Pair<Pair<Int, Int>, Char>, Long>()


    while(stack.isNotEmpty()) {
        val last = stack.removeFirst()

        if((seen[last.position to last.direction] ?: Long.MAX_VALUE) < last.score) {
            // this is already not best solution to abort it
            continue
        } else {
            seen[last.position to last.direction] = last.score
        }

        if(last.moves.contains(last.position)) {
            continue
        }

        if(walls.contains(last.position)) {
            // is in wall
            continue
        }

        if(last.position == end) {
            // finished
            if (top > last.score ){
                top = last.score
                bestPaths.removeAll { true }
                bestPaths.add(last)
            }

            if(top == last.score) {
                bestPaths.add(last)
            }
            continue
        }

        WAYS[last.direction]!!.forEach { direction ->
            val newMoves = last.moves.toMutableList()
            newMoves.add(last.position)
            if(direction.first != last.direction) {
                stack.add(State(direction.first, last.position.add(direction.second), newMoves, last.turns + 1))
            } else {
                stack.addFirst(State(direction.first, last.position.add(direction.second), newMoves, last.turns))
            }
        }
        index += 1
    }

    return bestPaths.first().score to bestPaths.flatMap { it.moves }.toSet().count().toLong() + 1
}

private data class State(
    val direction: Char,
    val position: Pair<Int, Int>,
    val moves: MutableList<Pair<Int, Int>>,
    val turns: Long
) {
    val score = turns * 1000 + moves.size
}

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