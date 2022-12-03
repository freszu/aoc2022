fun main() {
    fun priorityForChar(duplicate: Char) = when {
        duplicate in 'a'..'z' -> duplicate.code - 'a'.code + 1
        duplicate in 'A'..'Z' -> duplicate.code - 'A'.code + 27
        else -> throw IllegalArgumentException("Invalid character")
    }

    fun part1(input: List<String>) = input.map { rucksack -> rucksack.chunked(rucksack.length / 2) }
        .map { (firstCompartment, secondCompartment) ->
            firstCompartment.toSet().intersect(secondCompartment.toSet())
        }
        .map { duplicates -> duplicates.map(::priorityForChar) }
        .sumOf(List<Int>::sum)

    fun part2(input: List<String>) = input.map(String::toSet)
        .chunked(3)
        .map { elfGroup ->
            elfGroup.reduce { acc, chars -> acc.intersect(chars) }
        }
        .map { duplicates -> duplicates.map(::priorityForChar) }
        .sumOf(List<Int>::sum)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val dayInput = readInput("Day03")
    part1(testInput)
    check(part1(testInput) == 157)
    println(part1(dayInput))

    check(part2(testInput) == 70)
    println(part2(dayInput))
}
