package day5

import java.io.File

fun main() {
    val input = File("src/day5", "day5_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 6384
    println("Stage 2 answer is ${stageTwo(input)}") // 5353
}

fun stageOne(input: List<String>): Int {
    val (rules, updates) = input.parseInput()

    return updates.filter { it.isValid(rules) }.sumOf {
        val part = it.split(",")
        part[(part.size - 1) / 2].toInt()
    }
}

fun stageTwo(input: List<String>): Int {
    val (rules, updates) = input.parseInput()

    return updates.filter { !it.isValid(rules) }
        .map { it.correct(rules) }
        .sumOf {
            val part = it.split(",")
            part[(part.size - 1) / 2].toInt()
        }
}

private fun String.isValid(rules: Map<Int, List<Int>>): Boolean {
    return this.correct(rules) == this
}

private fun String.correct(rules: Map<Int, List<Int>>): String {

    val split = this.split(",").map { it.toInt() }
    split.indices.forEach {
        val before = split.take(it) // take elements before
        val validatingRules = rules.getOrDefault(split[it], emptyList()).intersect(before.toSet())
        if (validatingRules.isNotEmpty()) { // if element is in incorrect place replace it with one causing validation fail
            val validatingIndex = split.indexOf(validatingRules.first())
            return split.swap(validatingIndex, it).joinToString(",").correct(rules)
        } else {
            if (it == split.size - 1) { // If it has reached end it means its valid
                return this
            }
        }
    }
    throw IllegalArgumentException("BOOM")
}

private fun List<Int>.swap(index1: Int, index2: Int): List<Int> {
    val temp = this[index1]
    val mutable = this.toMutableList()
    mutable[index1] = this[index2]
    mutable[index2] = temp
    return mutable
}

private fun List<String>.parseInput(): Pair<Map<Int, List<Int>>, List<String>> {
    val rules = this
        .takeWhile { it.isNotEmpty() }
        .map {
            it.split("|")
                .let { it[0] to it[1] }
        }
        .groupBy { it.first }
        .map { it.key.toInt() to it.value.map { it.second.toInt() } }
        .toMap()

    val updates = this.dropWhile { it.contains("|") }.drop(1)

    return rules to updates
}