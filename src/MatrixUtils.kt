typealias Matrix<T> = List<List<T>>

inline fun <T, R> Matrix<T>.map2d(transform: (T) -> R) = this.map { it.map(transform) }

inline fun <T, R> Matrix<T>.map2dIndexed(transform: (x: Int, y: Int, T) -> R) = mapIndexed { y, rows ->
    rows.mapIndexed { x, t -> transform(x, y, t) }
}

operator fun <T> Matrix<T>.get(position: Position): T = this[position.y][position.x]

fun <T> Matrix<T>.positionOf(value: T): Position {
    var x: Int = -1
    val y = indexOfFirst {
        x = it.indexOf(value)
        x != -1
    }
    return x to y
}

fun <T> Matrix<T>.replace(position: Position, value: T): Matrix<T> {
    return this.toMutableList().apply {
        this[position.y] = this[position.y].toMutableList().apply {
            this[position.x] = value
        }
    }
}

fun <T> Matrix<T>.getOrNull(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)

/**
 * Left, top, right, bottom
 */
fun <T> Matrix<T>.neighbors(position: Position): Sequence<Position> {
    val (x, y) = position

    return sequenceOf(x - 1 to y, x to y + 1, x + 1 to y, x to y - 1)
        .filter { getOrNull(it.x, it.y) != null }
}

fun <T> Matrix<T>.findAll(value: T) = sequence {
    for (y in indices) {
        for (x in this@findAll[y].indices) {
            if (this@findAll[y][x] == value) yield(x to y)
        }
    }
}

fun <T> Matrix<T>.nicePrint() = joinToString("\n")

typealias Position = Pair<Int, Int>

val Pair<Int, Int>.x: Int
    get() = this.first

val Pair<Int, Int>.y: Int
    get() = this.second

