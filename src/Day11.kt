private val monkeyRegex = Regex(
    "Monkey (\\d):\\n" +
            "  Starting items: ([\\d+, ]*)\\n" +
            "  Operation: new = old ([^\\n]*)\\n" +
            "  Test: divisible by (\\d+)\\n" +
            "    If true: throw to monkey (\\d+)\\n" +
            "    If false: throw to monkey (\\d+)"
)

private typealias WorryLevel = Long

private data class Monkey(
    val items: MutableList<WorryLevel>,
    val operation: (WorryLevel) -> WorryLevel,
    val test: Long,
    val trueTarget: Int,
    val falseTarget: Int,
    var inspectedItemsCounter: Long = 0
)

fun main() {

    fun buildOperationFromString(fromString: String): (WorryLevel) -> WorryLevel {
        val (opp, timesString) = fromString.split(" ")
        val operation: (WorryLevel, WorryLevel) -> WorryLevel = when (opp) {
            "*" -> Long::times
            "+" -> Long::plus
            else -> error("Unknown operation $opp")
        }
        val times = timesString.toLongOrNull()
        return { old ->
            if (times == null) operation(old, old)
            else operation(old, times)
        }
    }

    fun parseMonkeys(input: String) = input.split("\n\n")
        .associate {
            val (id, items, operation, test, trueTarget, falseTarget) = monkeyRegex.matchEntire(it)!!.destructured
            id.toInt() to Monkey(
                items.split(", ").map(String::toLong).toMutableList(),
                buildOperationFromString(operation),
                test.toLong(),
                trueTarget.toInt(),
                falseTarget.toInt()
            )
        }

    fun runMonkeyRounds(monkeys: Map<Int, Monkey>, times: Int, worryLevelDivider: WorryLevel = 1): Long {
        // part2 optimisation
        val mod = monkeys.map { it.value.test }.reduce(Long::times)
        repeat(times) {
            monkeys.forEach { (_, monkey) ->
                val newItems = monkey.items.map(monkey.operation).map { it / worryLevelDivider % mod }
                val (trueItems, falseItems) = newItems.partition { it % monkey.test == 0L }
                monkeys[monkey.trueTarget]?.items?.addAll(trueItems)
                monkeys[monkey.falseTarget]?.items?.addAll(falseItems)

                monkey.inspectedItemsCounter = monkey.inspectedItemsCounter + monkey.items.size
                monkey.items.clear()
            }
        }

        return monkeys.map { it.value.inspectedItemsCounter }.sortedDescending().take(2).reduce(Long::times)
    }

    val testInput = readInputAsString("Day11_test").dropLast(1)
    val input = readInputAsString("Day11").dropLast(1)

    check(runMonkeyRounds(parseMonkeys(testInput), 20, 3) == 10605L)
    println("""Monkey business level: ${runMonkeyRounds(parseMonkeys(input), 20, 3)}""")

    check(runMonkeyRounds(parseMonkeys(testInput), 10000) == 2713310158)
    println("""Monkey business level: ${runMonkeyRounds(parseMonkeys(input), 10000)}""")
}
