private enum class Move {
    ROCK, PAPER, SCISSORS;

    fun score(against: Move) = when {
        this == against -> 3
        this.losingAgainst() == against -> 0
        this.winsAgainst() == against -> 6
        else -> throw IllegalStateException("Invalid move combination: $this vs $against")
    }

    fun losingAgainst() = Move.values()[(this.ordinal + 1) % Move.values().size]
    fun winsAgainst() = Move.values()[(this.ordinal + 2) % Move.values().size]
    fun scoreForMove() = this.ordinal + 1

    companion object {
        fun fromChar(char: Char) = when (char) {
            'A', 'X' -> ROCK
            'B', 'Y' -> PAPER
            'C', 'Z' -> SCISSORS
            else -> throw IllegalArgumentException("Invalid move")
        }

        fun fromElfCode(opponentMove: Move, expectedResult: Char) = when (expectedResult) {
            'X' -> opponentMove.winsAgainst()
            'Y' -> opponentMove
            'Z' -> opponentMove.losingAgainst()
            else -> throw IllegalArgumentException("Invalid result")
        }
    }
}

fun main() {
    fun part1(input: List<String>) = input.map {
        val opponentMove = Move.fromChar(it[0])
        val myMove = Move.fromChar(it[2])
        myMove.scoreForMove() + myMove.score(against = opponentMove)
    }.sum()

    fun part2(input: List<String>) = input.map {
        val opponentMove = Move.fromChar(it[0])
        val myMove = Move.fromElfCode(opponentMove, it[2])
        myMove.scoreForMove() + myMove.score(against = opponentMove)
    }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val dayInput = readInput("Day02")
    check(part1(testInput) == 15)
    println(part1(dayInput))

    check(part2(testInput) == 12)
    println(part2(dayInput))
}
