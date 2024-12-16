import java.io.File
import java.io.FileNotFoundException
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

object Runner {

    @OptIn(ExperimentalTime::class)
    fun run(day: String, answers: Map<String, Long>, stageOne: (List<String>) -> Long, stageTwo: (List<String>) -> Long) {
        val stageInput = File("src/day${day}", "day${day}_input.txt").readLines()

        try {
            val testInput = File("src/day${day}", "day${day}_test_input.txt").readLines()
            stageOne(testInput).also {
                assert( it == answers["TEST1_PART1"]) { "Test for part 1 failed. Is: $it, should be ${answers["TEST1_PART1"]}" }
            }
            println("Done test 1 part 1")
        } catch (_: FileNotFoundException) {}

        try {
            val testInput = File("src/day${day}", "day${day}_test_input2.txt").readLines()
            stageOne(testInput).also {
                assert( it == answers["TEST2_PART1"]) { "Second test for part 1 failed. Is: $it, should be ${answers["TEST2_PART1"]}" }
            }
            println("Done test 2 part 1")
        } catch (_: FileNotFoundException) {}


        val (stageOneResult, stageOneTime) = measureTimedValue {
            stageOne(stageInput)
        }
        println("Stage 1 answer is $stageOneResult done in $stageOneTime ms")


        try {
            val testInput = File("src/day${day}", "day${day}_test_input.txt").readLines()
            stageTwo(testInput).also {
                assert( it == answers["TEST1_PART2"]) { "Test for part 2 failed. Is: $it, should be ${answers["TEST1_PART2"]}" }
            }
            println("Done test 1 part 2")
        } catch (_: FileNotFoundException) {}

        try {
            val testInput = File("src/day${day}", "day${day}_test_input2.txt").readLines()
            stageTwo(testInput).also {
                assert( it == answers["TEST2_PART2"]) { "Second test for part 2 failed. Is: $it, should be ${answers["TEST2_PART2"]}" }
            }
            println("Done test 2 part 2")
        } catch (_: FileNotFoundException) {}


        val (stageTwoResult, stageTwoTime) = measureTimedValue {
            stageTwo(stageInput)
        }
        println("Stage 2 answer is $stageTwoResult done in $stageTwoTime ms")

    }
}