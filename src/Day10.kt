fun main() {

    fun xOverCycles(lines: List<String>) = lines.map {
        val split = it.split(" ")
        split.first() to split.getOrNull(1)?.toInt()
    }
        .fold(listOf(1)) { acc, (op, arg) ->
            when (op) {
                "noop" -> acc + (acc.last())
                "addx" -> acc + acc.last() + (acc.last() + requireNotNull(arg))
                else -> error("Unknown op $op")
            }
        }

    fun part1(xInCycles: List<Int>) = xInCycles.foldIndexed(0) { index, acc, v ->
        val cycle = index + 1
        if (cycle in (20..220 step 40)) acc + v * cycle else acc
    }

    fun part2(xInCycles: List<Int>): List<String> {
        // 40x6 CRT
        val crt = Array(6) { crtRow ->
            CharArray(40) { crtColumn ->
                val spriteAt = xInCycles[crtRow * 40 + crtColumn] - 1
                val drawn = if (crtColumn in spriteAt..spriteAt + 2) '#' else '.'
                drawn
            }
        }
        return crt.map { it.joinToString("") }

    }

    val testInput = xOverCycles(readInput("Day10_test"))
    val input = xOverCycles(readInput("Day10"))

    check(part1(testInput) == 13140)
    println(part1(input))

    val part2expectedTest = "##..##..##..##..##..##..##..##..##..##..\n" +
            "###...###...###...###...###...###...###.\n" +
            "####....####....####....####....####....\n" +
            "#####.....#####.....#####.....#####.....\n" +
            "######......######......######......####\n" +
            "#######.......#######.......#######....."
    check(part2(testInput).joinToString("\n") == part2expectedTest)
    println(
        part2(input).joinToString("\n")
            .replace("#", "█")
            .replace(".", " ")
    )
}
