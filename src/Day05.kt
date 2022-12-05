/**
 * For [A,B,C,D,E,F] updated list [D,E,F] and returned list [A,B,C]
 */
private fun <T> MutableList<T>.removeFirst(count: Int) = List(count) { removeAt(0) }

private data class CraneMove(val count: Int, val from: Int, val to: Int)

private fun parse(input: String): Pair<List<ArrayDeque<Char>>, List<CraneMove>> {
    val (stackDescription, movesDescription) = input.split("\n\n")
    return parseStacks(stackDescription) to parseMoves(movesDescription)
}

private fun parseStacks(stackDescription: String): List<ArrayDeque<Char>> {
    val stackDescriptionRows = stackDescription.split("\n").dropLast(1)
    val range = 1..stackDescriptionRows.first().length step 4
    val stacks = List(range.count()) { ArrayDeque<Char>() }
    stackDescriptionRows.forEach { line ->
        for (i in range) {
            line[i].takeIf { it.isLetter() }
                ?.run { stacks[range.indexOf(i)].add(line[i]) }
        }
    }
    return stacks
}

private fun parseMoves(movesDescription: String): List<CraneMove> {
    val moveRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")
    return movesDescription
        .split("\n")
        .dropLast(1)
        .map { line ->
            val (count, from, to) = moveRegex.matchEntire(line)!!.destructured
            CraneMove(count.toInt(), from.toInt() - 1, to.toInt() - 1)
        }
}

fun main() {

    fun crateMover9000(input: String): String {
        val (stacks, moves) = parse(input)
        moves.forEach { (count, from, to) ->
            repeat(count) {
                stacks[to].addFirst(stacks[from].removeFirst())
            }
        }
        return stacks.map { it.first() }.joinToString("")
    }

    fun crateMover9001(input: String): String {
        val (stacks, moves) = parse(input)
        moves.forEach { (count, from, to) ->
            stacks[to].addAll(0, stacks[from].removeFirst(count))
        }
        return stacks.map { it.first() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("Day05_test")
    val dayInput = readInputAsString("Day05")

    check(crateMover9000(testInput) == "CMZ") {
        "got ${crateMover9000(testInput)}"
    }
    println(crateMover9000(dayInput))


    check(crateMover9001(testInput) == "MCD")
    println(crateMover9001(dayInput))
}
