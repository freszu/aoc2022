import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

sealed interface Packet : Comparable<Packet> {
    @JvmInline
    value class IntValue(val int: Int) : Packet {
        override fun compareTo(other: Packet): Int = when (other) {
            is IntValue -> int.compareTo(other.int)
            is ListValue -> ListValue(listOf(this)).compareTo(other)
        }
    }

    @JvmInline
    value class ListValue(val packets: List<Packet>) : Packet {
        override fun compareTo(other: Packet): Int {
            when (other) {
                is IntValue -> return this.compareTo(ListValue(listOf(other)))
                is ListValue -> {
                    this.packets.indices.forEach { index ->
                        if (index > other.packets.lastIndex) return 1
                        val comparison = this.packets[index].compareTo(other.packets[index])
                        if (comparison != 0) return comparison
                    }
                    if (this.packets.size < other.packets.size) return -1
                }
            }
            return 0
        }
    }
}

fun main() {
    fun parsePacket(input: String): Packet {
        // surrender - cant skip this additional step even using value classes:
        // https://github.com/Kotlin/kotlinx.serialization/issues/2049#issuecomment-1271536271
        fun parseJsonArray(jsonElement: JsonElement): Packet {
            return when (jsonElement) {
                is JsonArray -> Packet.ListValue(jsonElement.map { parseJsonArray(it) })
                is JsonPrimitive -> Packet.IntValue(jsonElement.int)
                else -> throw IllegalArgumentException("Unknown json element: $jsonElement")
            }
        }

        val jsonArray = Json.decodeFromString<JsonArray>(input)
        return parseJsonArray(jsonArray)
    }

    fun part1(input: String) = input.split("\n\n")
        .map {
            val (leftPacket, rightPacket) = it.split("\n")
            parsePacket(leftPacket) to parsePacket(rightPacket)
        }
        .withIndex()
        .sumOf { (index, pair) ->
            val (left, right) = pair
            if (left <= right) index + 1 else 0
        }

    fun part2(input: List<String>): Int {
        val divider1 = parsePacket("[[2]]")
        val divider2 = parsePacket("[[6]]")

        val sorted = input.asSequence()
            .filter(String::isNotBlank)
            .map(::parsePacket)
            .plus(sequenceOf(divider1, divider2))
            .sorted()

        return (1 + sorted.indexOf(divider1)) * (1 + sorted.indexOf(divider2))
    }

    val testInput = readInputAsString("Day13_test")
    val input = readInputAsString("Day13")

    val testInputLines = readInput("Day13_test")
    val inputLines = readInput("Day13")

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInputLines) == 140)
    println(part2(inputLines))
}
