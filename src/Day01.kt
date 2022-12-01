fun main() {
    fun part1(input: List<String>) = input.map { it.toIntOrNull() }
        .fold(0 to 0) { (max, current), value ->
            if (value != null) {
                Pair(max, current + value)
            } else {
                Pair(if (current > max) current else max, 0)
            }
        }.first

    fun part2(inputFileName: String): Int = readInputAsString(inputFileName)
        .split("\n\n")
        .map { it.split("\n") }
        .map { elf -> elf.map { it.toIntOrNull() ?: 0 }.sum() }
        .sortedDescending()
        .take(3)
        .sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    println(part1(readInput("Day01")))

    check(part2("Day01_test") == 45000)
    println(part2("Day01"))
}
