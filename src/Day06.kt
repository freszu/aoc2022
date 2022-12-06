private const val START_PACKAGE = 4
private const val START_MESSAGE = 14

fun main() {
    fun part1(input: String) =
        input.windowed(START_PACKAGE).indexOfFirst { it.toSet().size == it.length } + START_PACKAGE

    fun part2(input: String) =
        input.windowed(START_MESSAGE).indexOfFirst { it.toSet().size == it.length } + START_MESSAGE

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("Day06_test")
    val dayInput = readInputAsString("Day06")

    check(part1(testInput) == 10)
    println(part1(dayInput))

    check(part2(testInput) == 29)
    println(part2(dayInput))
}
