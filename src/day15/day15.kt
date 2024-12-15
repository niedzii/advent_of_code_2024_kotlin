package day15

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val input = File("src/day15", "day15_input.txt").readLines()
    val time = measureTime {
        println("Stage 1 answer is ${stageOne(input)}") // 1526673
        println("Stage 2 answer is ${stageTwo(input)}") // 1535509
    }
    println("Both stages done in $time")
}

fun stageOne(input: List<String>): Int {
    val (walls, robot, boxes, instructions) = input.parseInput()
    var robotPosition = robot

    instructions.forEach {
        val direction = DIRECTIONS[it]!!
        val nextMove = robotPosition.add(direction)

        if (walls.contains(nextMove)) {
            // there is wall, do nothing
        } else {
            if (boxes.contains(nextMove)) {
                val (canBeMoved, boxesToMove) = nextMove.canBeMovedPartOne(boxes, walls, direction, mutableListOf())

                if (canBeMoved) {
                    boxes.removeAll(boxesToMove)
                    boxes.addAll(boxesToMove.map { it.add(direction) })
                    robotPosition = nextMove
                }
            } else {
                robotPosition = nextMove
            }
        }
    }

    return boxes.sumOf { it.second * 100 + it.first }
}

fun stageTwo(input: List<String>): Int {
    val (walls, robot, boxes, instructions) = input.parseInputPartTwo()

    var robotPosition = robot

    var moves = 0
    instructions.forEachIndexed { index, it ->
        val direction = DIRECTIONS[it]!!
        val nextMove = robotPosition.add(direction)

        if (walls.contains(nextMove)) {
            // there is wall, do nothing
        } else {
            if (boxes.flatMap { listOf(it.left, it.right) }.contains(nextMove)) {
                val (canBeMoved, boxesToMove) = nextMove.canBeMovedPartTwo(boxes, walls, direction, mutableSetOf())

                if (canBeMoved) {
                    boxes.removeAll(boxesToMove)
                    boxes.addAll(boxesToMove.map { it.add(direction) })
                    moves += boxesToMove.size
                    robotPosition = nextMove
                }
            } else {
                robotPosition = nextMove
            }
        }

    }
    return boxes.sumOf { it.left.second * 100 + it.left.first }
}

private fun Pair<Int, Int>.canBeMovedPartOne(
    boxes: Boxes,
    walls: Walls,
    direction: Pair<Int, Int>,
    movedBoxes: MutableList<Pair<Int, Int>>
): Pair<Boolean, List<Pair<Int, Int>>> {
    if (walls.contains(this)) {
        // there is wall, cannot be moved
        return false to emptyList()
    }
    return if (!boxes.contains(this)) {
        // there is empty space, can be moved
        true to movedBoxes
    } else {
        movedBoxes.add(this)
        this.add(direction).canBeMovedPartOne(boxes, walls, direction, movedBoxes)
    }
}

private fun Pair<Int, Int>.canBeMovedPartTwo(
    boxes: MutableSet<Box>,
    walls: Walls,
    direction: Pair<Int, Int>,
    movedBoxes: MutableSet<Box>
): Pair<Boolean, Set<Box>> {
    if (walls.contains(this)) {
        // there is wall, cannot be moved
        return false to emptySet()
    }

    val boxOccupiedSpots = boxes.flatMap { it.bothPoints }
    return if (!boxOccupiedSpots.contains(this)) {
        // there is empty space, can be moved
        true to movedBoxes
    } else {
        val collisionBox = boxes.find { it.bothPoints.contains(this) }!!
        movedBoxes.add(collisionBox)

        collisionBox.canBeMovedPartTwo(boxes, walls, direction, movedBoxes)
    }
}

// this is without adding
private fun Box.canBeMovedPartTwo(
    boxes: MutableSet<Box>,
    walls: Walls,
    direction: Pair<Int, Int>,
    movedBoxes: MutableSet<Box>
): Pair<Boolean, Set<Box>> {

    val nextPosition = this.add(direction)

    if (walls.intersect(nextPosition.bothPoints).isNotEmpty()) {
        // there is wall, cannot be moved
        return false to emptySet()
    }

    val boxOccupiedSpots = boxes.filter { it != this }.flatMap { it.bothPoints }

    return if (boxOccupiedSpots.intersect(nextPosition.bothPoints).isEmpty()) {
        // there is empty space, can be moved
        movedBoxes.add(this)
        true to movedBoxes
    } else {
        val collisionBoxes =
            boxes.filter { it != this }.filter { it.bothPoints.intersect(nextPosition.bothPoints).isNotEmpty() }

        if (collisionBoxes.size == 1) {
            movedBoxes.add(this)
            val canBeMovedPartTwo = collisionBoxes.first().canBeMovedPartTwo(boxes, walls, direction, movedBoxes)
            return canBeMovedPartTwo
        } else {
            // only 2
            movedBoxes.add(this)
            val (leftBool, leftBoxes) = collisionBoxes.first().canBeMovedPartTwo(boxes, walls, direction, movedBoxes)
            val (rightBool, rightBoxes) = collisionBoxes.last().canBeMovedPartTwo(boxes, walls, direction, movedBoxes)

            val combined = leftBoxes.toMutableList()
            combined.addAll(rightBoxes)
            combined.add(this)
            return (leftBool && rightBool) to combined.toSet()
        }
    }

}

private val DIRECTIONS =
    mapOf(
        '^' to (0 to -1),
        '>' to (1 to 0),
        'v' to (0 to 1),
        '<' to (-1 to 0)
    )

private fun List<String>.parseInput(): Quadruple<Walls, Robot, Boxes, Instructions> {
    val walls = mutableListOf<Pair<Int, Int>>()
    val boxes = mutableListOf<Pair<Int, Int>>()
    var robot: Pair<Int, Int>? = null
    this.forEachIndexed { yIndex, row ->
        row.forEachIndexed { xIndex, char ->
            when (char) {
                '#' -> walls.add(Pair(xIndex, yIndex))
                'O' -> boxes.add(Pair(xIndex, yIndex))
                '@' -> robot = xIndex to yIndex
            }
        }
    }
    val instructions = this.dropWhile { it.isBlank() || it[0] == '#' }.joinToString("").toCharArray()

    return Quadruple(walls, robot!!, boxes, instructions)
}

private fun List<String>.parseInputPartTwo(): Quadruple<Walls, Robot, MutableSet<Box>, Instructions> {
    val walls = mutableListOf<Pair<Int, Int>>()
    val boxes = mutableSetOf<Box>()
    var robot: Pair<Int, Int>? = null
    this.forEachIndexed { yIndex, row ->
        row.forEachIndexed { xIndex, char ->
            when (char) {
                '#' -> {
                    walls.add(Pair(xIndex * 2, yIndex))
                    walls.add(Pair(xIndex * 2 + 1, yIndex))
                }

                'O' -> {
                    boxes.add(
                        Box(xIndex * 2 to yIndex, xIndex * 2 + 1 to yIndex)
                    )
                }

                '@' -> robot = xIndex * 2 to yIndex
            }
        }
    }
    val instructions = this.dropWhile { it.isBlank() || it[0] == '#' }.joinToString("").toCharArray()

    return Quadruple(walls, robot!!, boxes, instructions)
}

typealias Walls = List<Pair<Int, Int>>
typealias Robot = Pair<Int, Int>
typealias Boxes = MutableList<Pair<Int, Int>>
typealias Instructions = CharArray

private data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

private data class Box(
    val left: Pair<Int, Int>,
    val right: Pair<Int, Int>,
) {
    val bothPoints = setOf(this.left, this.right)

    fun add(direction: Pair<Int, Int>): Box {
        return Box(left.add(direction), right.add(direction))
    }
}

private fun Pair<Int, Int>.add(another: Pair<Int, Int>) = this.first + another.first to this.second + another.second

private fun print(walls: Walls, robot: Robot, boxes: Boxes) {
    val maxX = walls.maxOf { it.first }
    val minX = walls.minOf { it.first }
    val maxY = walls.maxOf { it.second }
    val minY = walls.minOf { it.second }

    (minY..maxY).forEach { yIndex ->
        (minX..maxX).forEach { xIndex ->
            if (walls.contains(xIndex to yIndex)) {
                print("#")
            } else if (boxes.contains(xIndex to yIndex)) {
                print("O")
            } else if (robot == xIndex to yIndex) {
                print("@")
            } else {
                print(".")
            }
        }
        print("\n")
    }
}

private fun printPartTwo(walls: Walls, robot: Robot, boxes: MutableSet<Box>) {
    val maxX = walls.maxOf { it.first }
    val minX = walls.minOf { it.first }
    val maxY = walls.maxOf { it.second }
    val minY = walls.minOf { it.second }
    (minY..maxY).forEach { yIndex ->
        print(yIndex)

        (minX..maxX).forEach { xIndex ->
            if (walls.contains(xIndex to yIndex)) {
                print("#")
            } else if (boxes.map { it.left }.contains(xIndex to yIndex)) {
                print("[")
            } else if (boxes.map { it.right }.contains(xIndex to yIndex)) {
                print("]")
            } else if (robot == xIndex to yIndex) {
                print("@")
            } else {
                print(".")
            }
        }
        print("\n")
    }
}