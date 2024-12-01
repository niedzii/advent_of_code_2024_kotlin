package utils

/**
 *       \/ y
 *  x > [0][1][2][3]
 *      [1]
 *      [2]
 *      [3]
 *      [4]
 */
open class Grid<T>(height: Int, width: Int, init: (index: Int) -> T) {

    private val asList = MutableList(height) { MutableList(width, init) }

    fun get(x: Int, y: Int): T {
        return asList[y][x]
    }

    fun getOrNull(x: Int, y: Int): T? {
        return try {
            asList[y][x]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun put(x: Int, y: Int, ele: T) {
        asList[y][x] = ele
    }

    fun print(translation: Map<T, Char>, yWithColor: List<Int> = emptyList()) {
        print("  ")
        (0 until asList.first().size).forEach {
            if(it % 5 == 0) {
                print(it % 10)
            } else {
                print(" ")
            }
        }
        print("\n")


        asList.forEachIndexed { column, _ ->
            if(column % 5 == 0) {
                print("${column % 10} ")
            } else {
                print("  ")
            }
            asList[column].forEachIndexed { index, rowElement ->
                val element = translation[rowElement] ?: rowElement
                if(yWithColor.contains(index)) {
                    print("\u001B[31m$element\u001B[0m")
                } else {
                    print(element)
                }
            }
            print("\n")
        }
    }
}