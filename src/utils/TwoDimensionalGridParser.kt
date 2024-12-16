package utils


object TwoDimensionalGridParser {

    fun parse(
        input: List<String>,
        wallsChar: Char = '#',
        specificChars: List<Char> = emptyList()
    ): TwoDimensionalGridResult {

        val charsAsMap = specificChars.associateWith { mutableListOf<Pair<Int, Int>>() }.toMutableMap()
        charsAsMap[wallsChar] = mutableListOf()

        input.forEachIndexed { yIndex, row ->
            row.forEachIndexed { xIndex, char ->
                charsAsMap.get(char)?.add(Pair(xIndex, yIndex))
            }
        }

        return TwoDimensionalGridResult(charsAsMap[wallsChar]!!, charsAsMap)
    }

    data class TwoDimensionalGridResult(
        val walls: List<Pair<Int, Int>>,
        val specialCharsFound: Map<Char, List<Pair<Int, Int>>>
    )
}
