package day6

import java.io.File

fun main() {
    val input = File("src/day6", "day6_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 5318
    println("Stage 2 answer is ${stageTwo(input)}") // 1831
}

private val DIRECTIONS = listOf(
    0 to -1, // up
    1 to 0, // right
    0 to 1, // left
    -1 to 0 // right
)

fun stageOne(input: List<String>): Int {
    return getVisitedFields(input).size
}

fun stageTwo(input: List<String>): Int {
    val visitedFields = getVisitedFields(input).toMutableSet().drop(1) // drop first as first is his starting position
    return visitedFields.toSet().filter { checkIfLoop(it, input) }.toSet().count()
}

private fun Pair<Int, Int>.add(another: Pair<Int, Int>) = this.first + another.first to this.second + another.second

private fun checkIfLoop(
    newObstacle: Pair<Int, Int>,
    input: List<String>
): Boolean {
    val (guardianStartingPosition, obstacles) = parseInput(input, newObstacle)

    val visitedFields = mutableListOf<Triple<Int, Int, Int>>()
    var currentGuardianPosition = guardianStartingPosition
    var currentDirectionIndex = 0

    while (true) {
        val currentDirection = DIRECTIONS[currentDirectionIndex]
        visitedFields.add(Triple(currentGuardianPosition.first, currentGuardianPosition.second, currentDirectionIndex))
        val nextStep = currentGuardianPosition.add(currentDirection)

        if (nextStepIsOutOfBounds(nextStep, input)) {
            // he is out
            return false
        }
        if (visitedFields.contains(Triple(nextStep.first, nextStep.second, currentDirectionIndex))) {
            // if he is in same field with same direction facing it means he has looped
            return true
        }

        if (obstacles.contains(nextStep)) {
            // rotate
            currentDirectionIndex = (currentDirectionIndex + 1) % 4
        } else {
            currentGuardianPosition = nextStep
        }
    }
}

private fun nextStepIsOutOfBounds(
    nextStep: Pair<Int, Int>,
    input: List<String>
) =
    nextStep.first < 0 || nextStep.first > input.first().length || nextStep.second < 0 || nextStep.second > input.size - 1

private fun parseInput(
    input: List<String>,
    newObstacle: Pair<Int, Int>? = null
): Pair<Pair<Int, Int>, MutableList<Pair<Int, Int>>> {
    var guardianStartingPosition = -1 to -1
    input.forEachIndexed { index, s ->
        if (s.contains("^")) {
            guardianStartingPosition = s.indexOf("^") to index
        }
    }

    val obstacles = mutableListOf<Pair<Int, Int>>()

    input.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columntIndex, element ->
            if (element == '#') {
                obstacles.add(columntIndex to rowIndex)
            }
        }
    }

    if (newObstacle != null) {
        obstacles.add(newObstacle)
    }
    return guardianStartingPosition to obstacles
}

private fun getVisitedFields(input: List<String>): Set<Pair<Int, Int>> {
    val (guardianStartingPosition, obstacles) = parseInput(input)

    val visitedFields = mutableListOf<Pair<Int, Int>>()
    var currentGuardianPosition = guardianStartingPosition
    var currentDirectionIndex = 0

    while (true) {
        val currentDirection = DIRECTIONS[currentDirectionIndex]
        visitedFields.add(currentGuardianPosition)
        val nextStep = currentGuardianPosition.add(currentDirection)

        if (nextStepIsOutOfBounds(nextStep, input)) {
            // he is out
            return visitedFields.toSet()
        }
        if (obstacles.contains(nextStep)) {
            // rotate
            currentDirectionIndex = (currentDirectionIndex + 1) % 4
        } else {
            currentGuardianPosition = nextStep
        }
    }
}