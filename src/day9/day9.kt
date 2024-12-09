package day9

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day9", "day9_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 6299243228569
        println("Stage 2 answer is ${stageTwo(input)}") // 6326952672104
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Long {
    val (files, freeSpace) = input.computeInput()

    val computed = mutableListOf<String>()

    files.indices.forEach { i ->
        repeat(files[i].digitToInt()) { computed.add("$i") }
        if (freeSpace.size > i) {
            repeat(freeSpace[i].digitToInt()) { computed.add(".") }
        }
    }

    while (true) {
        val temp = computed.last()

        val freeSpaceIndex = computed.indexOfFirst { it == "." }

        if (freeSpaceIndex == -1) {
            break
        }

        computed[freeSpaceIndex] = temp
        computed.removeLast()
    }
    return computed.calculateCheckSum()
}

fun stageTwo(input: List<String>): Long {
    val computed = input.computeInputButGoodThisTime()
    var changes = 0
    var counter = 0

    while (counter < computed.size - 1) {
        val last = computed[computed.size - 1 - counter]
        if (last.second != ".") {
            val openSpace =
                computed.filter { it.second == "." && it.first.size() >= last.first.size() }.sortedBy { it.first.start }
                    .firstOrNull()
            if (openSpace != null) {
                // swap
                if (openSpace.first.first < last.first.first) {
                    computed[computed.size - 1 - counter] =
                        last.first to openSpace.second // insert empty space where was occupied

                    if (openSpace.first.size() == last.first.size()) {
                        // just insert
                        changes += 1
                        computed[computed.indexOf(openSpace)] = openSpace.first to last.second
                    } else {
                        // insert but add remaining
                        val diff = openSpace.first.size() - last.first.size()
                        computed[computed.indexOf(openSpace)] =
                            IntRange(
                                openSpace.first.start,
                                openSpace.first.start + last.first.size() - 1
                            ) to last.second
                        computed.add(
                            IntRange(
                                openSpace.first.start + last.first.size(),
                                openSpace.first.start + last.first.size() - 1 + diff
                            ) to "."
                        )
                        changes += 1

                    }
                }
            }
        }
        counter += 1
    }
    return computed.calculateCheckSumTwo()
}

private fun MutableList<Pair<IntRange, String>>.calculateCheckSumTwo(): Long {
    var result = 0L

    this
        .filter { it.second.isNotBlank() && !it.second.contains(".") }
        .forEach { outter ->
            outter.first.forEach {
                result += it * outter.second.toLong()
            }
        }

    return result
}


private fun IntRange.size() = this.last - this.first + 1

private fun List<String>.calculateCheckSum(): Long {
    var result = 0L

    this.forEachIndexed { index, s ->
        s.takeIf { it != "." }?.run {
            result += index * s.toLong()
        }
    }

    return result
}

private fun List<String>.computeInput(): Pair<MutableList<Char>, MutableList<Char>> {
    val files = mutableListOf<Char>()
    val freeSpace = mutableListOf<Char>()
    this.joinToString("")
        .forEachIndexed { index, c ->
            if (index % 2 == 0) {
                files.add(c)
            } else {
                freeSpace.add(c)
            }
        }
    return files to freeSpace
}

private fun List<String>.computeInputButGoodThisTime(): MutableList<Pair<IntRange, String>> {
    val result = mutableListOf<Pair<IntRange, String>>()

    var currentIndex = 0
    this.first().forEachIndexed { index, c ->
        val asInt = c.digitToInt()
        if (index % 2 == 0) {
            result.add(Pair(IntRange(currentIndex, currentIndex + asInt - 1), "${index / 2}"))
            currentIndex += asInt
        } else {
            result.add(Pair(IntRange(currentIndex, currentIndex + asInt - 1), "."))
            currentIndex += asInt
        }
    }
    return result
}
