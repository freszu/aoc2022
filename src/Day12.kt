import java.util.*

private const val START = 'S'
private const val END = 'E'

fun main() {

    fun Matrix<Char>.dijkstraShortestPathLength(
        start: Position,
        end: Position
    ): Int? {
        val toBeEvaluated = PriorityQueue<Pair<Position, Int>>(compareBy { (_, distance) -> distance })
        toBeEvaluated.add(start to 0)

        val visited = mutableSetOf<Position>()

        while (toBeEvaluated.isNotEmpty()) {
            val (position, distance) = toBeEvaluated.poll()

            if (position !in visited) {
                if (position == end) return distance

                visited.add(position)

                neighbors(position)
                    .filter { get(position).code + 1 >= get(it).code }
                    .filter { it !in visited }
                    .forEach { neighbor -> toBeEvaluated.add(neighbor to distance + 1) }
            }
        }
        return null
    }

    fun part1(lines: List<String>): Int {
        val terrainMap = lines.map { it.toList() }
        val start = terrainMap.positionOf(START)
        val end = terrainMap.positionOf(END)

        return terrainMap
            .replace(start, 'a')
            .replace(end, 'z')
            .dijkstraShortestPathLength(start, end)
            .let(::requireNotNull)
    }

    fun part2(lines: List<String>): Int {
        val terrainMap = lines.map { it.toList() }
        val start = terrainMap.positionOf(START)
        val end = terrainMap.positionOf(END)
        val terrainWithoutMarkings = terrainMap.replace(start, 'a').replace(end, 'z')

        return terrainWithoutMarkings.findAll('a')
            .map { terrainWithoutMarkings.dijkstraShortestPathLength(it, end) }
            .filterNotNull()
            .min()
    }

    val testInput = readInput("Day12_test")
    val input = readInput("Day12")

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}
