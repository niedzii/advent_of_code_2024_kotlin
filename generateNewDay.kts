import java.time.LocalDate
import java.io.File
import java.nio.file.Paths

val day = LocalDate.now().dayOfMonth

val path = Paths.get("").toAbsolutePath().toString()
File("$path/src/day$day").mkdir()

val code = File("$path/src/day$day/day$day.kt")
val input = File("$path/src/day$day/day${day}_input.txt")

if(code.exists() || input.exists()) {
    throw Exception("Cannot override")
}

code.createNewFile()
input.createNewFile()

writeCode(code, day)

println("Done")

fun writeCode(file: File, day: Int) {
    file.appendText("package day$day\n")
    file.appendText("\n")
    file.appendText("import java.io.File\n")
    file.appendText("\n")
    file.appendText("fun main() {\n")
    file.appendText("    val input = File(\"src/day$day\", \"day${day}_input.txt\").readLines()\n")
    file.appendText("    println(\"Stage 1 answer is \${stageOne(input)}\") // \n")
    file.appendText("    println(\"Stage 2 answer is \${stageTwo(input)}\") // \n")
    file.appendText("}\n")
    file.appendText("\n")
    file.appendText("fun stageOne(input: List<String>): Int {\n")
    file.appendText("    return 0\n")
    file.appendText("}\n")
    file.appendText("\n")
    file.appendText("fun stageTwo(input: List<String>): Int {\n")
    file.appendText("    return 0\n")
    file.appendText("}\n")
}