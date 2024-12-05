package day4

import java.io.File

fun main() {
    val input = File("src/day4", "day4_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 2454
    println("Stage 2 answer is ${stageTwo(input)}") // 1858
}

fun stageOne(input: List<String>): Int {
    val ixes = mutableListOf<Pair<Int, Int>>()

    input.forEachIndexed { index, s ->
        for ((index1, c) in s.withIndex()) {
            if (c == 'X') {
                ixes.add(Pair(index, index1))
            }
        }
    }


    return ixes.sumOf { countWordsFromX(input, it) }
}

fun stageTwo(input: List<String>): Int {
    val letterAs = mutableListOf<Pair<Int, Int>>()

    input.forEachIndexed { index, s ->
        for ((index1, c) in s.withIndex()) {
            if (c == 'A') {
                letterAs.add(Pair(index, index1))
            }
        }
    }


    return letterAs.count { isCorrectX(input, it) }
}

private fun countWordsFromX(input: List<String>, xIndex: Pair<Int, Int>): Int {
    var result = 0
    val neighbours = setOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to 1,
        0 to -1,
        1 to -1,
        1 to 0,
        1 to 1
    )

    neighbours.forEach { (x, y) ->
        try {
            if (
                input.get(xIndex.first + x, xIndex.second + y) == 'M' &&
                input.get(xIndex.first + (x * 2), xIndex.second + (y * 2)) == 'A' &&
                input.get(xIndex.first + (x * 3), xIndex.second + (y * 3)) == 'S'
            ) {
                result += 1
            }
        } catch (e: IndexOutOfBoundsException) {
            //
        }
    }
    return result
}

private fun isCorrectX(input: List<String>, aIndex: Pair<Int, Int>): Boolean {
    val neighboursRight = setOf(
        -1 to -1,
        1 to 1
    )

    val neighboursLeft = setOf(
        -1 to 1,
        1 to -1
    )

    try {
        val right = neighboursRight.map { it.first + aIndex.first to it.second + aIndex.second }.map { input.get(it.first, it.second) }
        val left = neighboursLeft.map { it.first + aIndex.first to it.second + aIndex.second }.map { input.get(it.first, it.second) }
        return left.count { it == 'M' } == 1 && left.count { it == 'S' } == 1 && right.count { it == 'M' } == 1 && right.count { it == 'S' } == 1

    } catch (e: IndexOutOfBoundsException) {
        //
    }

    return false
}

private fun List<String>.get(x: Int, y: Int) = this[x][y]