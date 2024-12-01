package utils

fun main() {

    val grid = Grid(6, 10) { 0 }

    println("Before")
//    grid.print(mapOf(0 to '.', 1 to '@'))

    grid.put(0, 2, 1)
    grid.put(8, 4, 1)

    println("Should be true -> " + (grid.get(0, 0) == 0))
    println("Should be true -> " + (grid.get(0, 2) == 1))
    println("Should be true -> " + (grid.get(8, 4) == 1))


    grid.print(mapOf(0 to '.', 1 to '@'), listOf(5))
}