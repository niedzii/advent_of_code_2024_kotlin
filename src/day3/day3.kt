package day3

import java.io.File

fun main() {
    val input = File("src/day3", "day3_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 179834255
    println("Stage 2 answer is ${stageTwo(input)}") // 80570939
}

fun stageOne(input: List<String>): Int {
    val regex = "mul\\([0-9]{1,3},[0-9]{1,3}\\)".toRegex()
    return input.sumOf {
        regex.findAll(it).toList().map { it.value }.sumOf {
            calculateMult(it)
        }
    }
}

fun stageTwo(input: List<String>): Int {
    val regex = "mul\\([0-9]{1,3},[0-9]{1,3}\\)|do\\(\\)|don't\\(\\)".toRegex()
    val wholeInput = input.reduce { i1, i2 -> i1 + i2 }
    var result = 0
    var enabled = true

    regex.findAll(wholeInput).toList().map { it.value }.forEach {
        when (it) {
            "do()" -> {
                enabled = true
            }

            "don't()" -> {
                enabled = false
            }

            else -> {
                if (enabled) {
                    result += calculateMult(it)
                }
            }
        }
    }



    return result
}

private fun calculateMult(input: String): Int {
    val split = input.substring(4, input.length - 1).split(",")
    return split[0].toInt() * split[1].toInt()
}
