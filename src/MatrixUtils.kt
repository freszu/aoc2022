typealias Matrix<T> = List<List<T>>

inline fun <T, R> Matrix<T>.map2d(transform: (T) -> R) = this.map { it.map(transform) }

inline fun <T, R> Matrix<T>.map2dIndexed(transform: (x: Int, y: Int, T) -> R) = mapIndexed { y, rows ->
    rows.mapIndexed { x, t -> transform(x, y, t) }
}

fun <T> Matrix<T>.getOrNull(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)

fun <T> Matrix<T>.nicePrint() = joinToString("\n")

typealias Position = Pair<Int, Int>

val Pair<Int, Int>.x: Int
    get() = this.first

val Pair<Int, Int>.y: Int
    get() = this.second

