fun main() {
    fun part1(input: List<String>) = input.map { it.split('-', ',') }
        .map { it.map(String::toInt) }
        .map { (range1start, range1end, range2start, range2end) ->
            val range1 = range1start..range1end
            val range2 = range2start..range2end
            range1.contains(range2) || range2.contains(range1)
        }
        .count(true::equals)

    fun part2(input: List<String>) = input.map { it.split('-', ',') }
        .map { it.map(String::toInt) }
        .map { (range1start, range1end, range2start, range2end) ->
            val range1 = range1start..range1end
            val range2 = range2start..range2end
            range1.overlaps(range2)
        }
        .count(true::equals)

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val dayInput = readInput("Day04")
    part1(testInput)
    check(part1(testInput) == 2)
    println(part1(dayInput))

    check(part2(testInput) == 4)
    println(part2(dayInput))
}
