fun main() {
    fun parse(lines: List<String>): Set<Position> = lines.map { line ->
        line.split(" -> ")
            .map {
                val (x, y) = it.split(",")
                x.toInt() to y.toInt()
            }
            .windowed(2)
            .map { (p1, p2) -> p1.walkHorizontallyOrVertically(p2).toList() }
            .flatten()
    }
        .flatten()
        .toSortedSet(compareBy<Pair<Int, Int>> { it.first }.thenBy { it.second })

    tailrec fun sandFall(
        caveMap: Set<Position>,
        lowestYIsFloor: Boolean,
        particle: Position = 500 to 0,
        lowestY: Int = caveMap.maxOf { it.y }
    ): Set<Position> {
        val below = particle.copy(second = particle.y + 1)
        return when {
            particle in caveMap -> caveMap
            below.y > lowestY -> if (lowestYIsFloor) {
                sandFall(caveMap + particle, lowestYIsFloor, lowestY = lowestY)
            } else {
                caveMap
            }
            below !in caveMap -> sandFall(caveMap, lowestYIsFloor, below, lowestY)
            else -> {
                val left = below.copy(first = below.x - 1)
                val right = below.copy(first = below.x + 1)
                when {
                    left in caveMap && right in caveMap -> sandFall(
                        caveMap + particle, lowestYIsFloor, lowestY = lowestY
                    )
                    left !in caveMap -> sandFall(caveMap, lowestYIsFloor, left, lowestY)
                    right !in caveMap -> sandFall(caveMap, lowestYIsFloor, right, lowestY)
                    else -> error("Should not happen")
                }
            }
        }
    }

    fun part1(lines: List<String>): Int {
        val caveMap = parse(lines)
        val sandFall = sandFall(caveMap, lowestYIsFloor = false)
        return sandFall.count() - caveMap.count()
    }

    fun part2(lines: List<String>): Int {
        val caveMap = parse(lines)
        val sandFall = sandFall(
            caveMap, lowestY = (caveMap.maxOf { it.y } + 1), lowestYIsFloor = true
        )
        return sandFall.count() - caveMap.count()
    }

    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    check(part1(testInput) == 24)
    println(part1(input))

    check(part2(testInput) == 93)
    println(part2(input))

}
