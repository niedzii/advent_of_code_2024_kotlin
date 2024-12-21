package day19

const val isLoggerEnabled = true
private val ANSWERS = mapOf<String, Long>(
    "TEST1_PART1" to 6,
    "TEST1_PART2" to 16
)

private val possibleCache = mutableListOf<String>()
private val impossibleCache = mutableListOf<String>()
private val cache2 = mutableMapOf<String, Long>()

fun main() {
    Runner.run("19", ANSWERS, { stageOne(it) }, { stageTwo(it) })
}

fun stageOne(input: List<String>): Long {
    clearCache()
    val (towels, designs) = input.parseInput()
    return designs.count { howManyDesignsPossible(towels, it) > 0 }.toLong()
}

fun stageTwo(input: List<String>): Long {
    clearCache()
    val (towels, designs) = input.parseInput()
    return designs.sumOf { howManyDesignsPossible(towels, it) }
}

private fun clearCache() {
    possibleCache.clear()
    impossibleCache.clear()
    cache2.clear()
}

private fun howManyDesignsPossible(towels: List<String>, design: String): Long {
    var result = 0L

    if(cache2.containsKey(design)) {
        return cache2[design]!!
    }

    if(design.isBlank() || design.isEmpty()) {
        return 1
    }

    towels.forEach {
        if(design.startsWith(it)) {
            result += howManyDesignsPossible(towels, design.drop(it.length))
        }
    }

    cache2[design] = result
    return result
}

private fun List<String>.parseInput(): Pair<List<String>, List<String>> {
    return this.first().split(",").map { it.trim() } to this.drop(2)
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
