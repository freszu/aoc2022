import kotlin.math.sign

/**
 * All adjacent including diagonals + self
 */
private fun Position.adjacent() = sequenceOf(
    this,
    x - 1 to y, x + 1 to y, x to y - 1, x to y + 1,
    x - 1 to y - 1, x + 1 to y - 1, x - 1 to y + 1, x + 1 to y + 1
)

fun main() {

    fun headPositions(lines: List<String>): List<Position> = lines.map {
        val (direction, moveBy) = it.split(" ")
        direction to moveBy.toInt()
    }
        .fold((0 to 0) to emptyList<Pair<Int, Int>>()) { (h, moves), (direction, moveBy) ->
            val headPositions = (1..moveBy).map {
                when (direction) {
                    "U" -> h.x to h.y + it
                    "D" -> h.x to h.y - it
                    "L" -> h.x - it to h.y
                    "R" -> h.x + it to h.y
                    else -> error("Unknown direction $direction")
                }
            }
            (headPositions.last()) to (moves + headPositions)
        }.second

    fun List<Position>.follow() = scan(0 to 0) { t, h ->
        if (h in t.adjacent()) t else t.x + (h.x - t.x).sign to t.y + (h.y - t.y).sign
    }

    fun part1(lines: List<String>) = headPositions(lines)
        .follow()
        .toSet().size

    // ( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)
    fun part2(lines: List<String>) = headPositions(lines)
        .follow().follow().follow().follow().follow().follow().follow().follow().follow()
        .toSet().size

    val testInput = readInput("Day09_test")
    val testInput2 = readInput("Day09_test2")
    val input = readInput("Day09")

    part1(testInput)
    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 1)
    check(part2(testInput2) == 36)
    println(part2(input))
}
