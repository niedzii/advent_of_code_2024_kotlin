package day8

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day8", "day8_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 10741443549536
        println("Stage 2 answer is ${stageTwo(input)}") // 500335179214836
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Long {
    return 0
}

fun stageTwo(input: List<String>): Long {
    return 0
}
