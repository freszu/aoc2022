enum class Direction { LEFT, TOP, RIGHT, BOTTOM }

fun <T> Sequence<T>.takeWhileInclusive(predicate: (T) -> Boolean): Sequence<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = predicate(it)
        result
    }
}

fun <T> Matrix<T>.walkToEdge(fromX: Int, fromY: Int, direction: Direction): Sequence<T> = sequence {
    var offset = 1
    while (true) {
        val inLine = when (direction) {
            Direction.LEFT -> this@walkToEdge.getOrNull(fromX - offset, fromY)
            Direction.TOP -> this@walkToEdge.getOrNull(fromX, fromY - offset)
            Direction.RIGHT -> this@walkToEdge.getOrNull(fromX + offset, fromY)
            Direction.BOTTOM -> this@walkToEdge.getOrNull(fromX, fromY + offset)
        }
        if (inLine == null) break else yield(inLine)
        offset++
    }
}

fun main() {
    fun createHeightMap(input: List<String>) = input.map { line -> line.map(Char::digitToInt) }

    fun part1(matrix: Matrix<Int>) = matrix.map2dIndexed { x, y, treeHouseHeight ->
        Direction.values()
            .map { dir -> matrix.walkToEdge(x, y, dir).all { treeHeight -> treeHeight < treeHouseHeight } }
            .any(true::equals)
    }
        .flatten()
        .count(true::equals)

    fun part2(matrix: Matrix<Int>) = matrix.map2dIndexed { x, y, treeHouseHeight ->
        Direction.values()
            .map { dir ->
                matrix.walkToEdge(x, y, dir).takeWhileInclusive { treeHeight -> treeHouseHeight > treeHeight }.count()
            }
            .reduce(Int::times)
    }
        .flatten()
        .max()

    val testInput = createHeightMap(readInput("Day08_test"))
    val input = createHeightMap(readInput("Day08"))

    check(part1(testInput) == 21)
    println(part1(input))

    check(part2(testInput) == 8)
    println(part2(input))
}
