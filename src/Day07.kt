private sealed interface FileSystem {
    data class File(val name: String, val size: Int) : FileSystem

    data class Dir(val parent: Dir?, val name: String, val content: MutableList<FileSystem>) : FileSystem {

        fun contentFromLsOutput(line: List<String>) = line.map { fromLsOutputLine(it) }

        private fun fromLsOutputLine(line: String): FileSystem = when {
            line.startsWith("dir") -> {
                val (_, name) = line.split(" ")
                Dir(this@Dir, name, mutableListOf())
            }

            else -> {
                val (size, name) = line.split(" ")
                File(name, size.toInt())
            }
        }
    }
}

fun main() {
    fun parseFs(input: String): FileSystem {
        var lines = input.lines().drop(1).dropLast(1)
        val fs = FileSystem.Dir(null, "/", mutableListOf())
        var currentDir = fs
        while (lines.isNotEmpty()) {
            val lineSplit = lines.first().split(" ")
            lines = when (lineSplit[1]) {
                "ls" -> {
                    lines = lines.drop(1)
                    val resultEnd = lines.indexOfFirst { it.startsWith("$") }
                    val lsOutput = lines.subList(0, if (resultEnd == -1) lines.size else resultEnd)
                    val content = currentDir.contentFromLsOutput(lsOutput)
                    currentDir.content.addAll(content)
                    lines.drop(lsOutput.size)
                }

                "cd" -> {
                    currentDir = when (val arg = lineSplit[2]) {
                        ".." -> currentDir.parent!!
                        else -> currentDir.content.first {
                            it is FileSystem.Dir && it.name == arg
                        } as FileSystem.Dir
                    }
                    lines.drop(1)
                }

                else -> error("Unknown command ${lineSplit[1]}")
            }
        }
        return fs
    }

    fun FileSystem.walk(): Sequence<FileSystem> = sequence {
        yield(this@walk)
        if (this@walk is FileSystem.Dir) {
            yieldAll(content.asSequence().flatMap { it.walk() })
        }
    }

    fun FileSystem.size(): Int = this.walk().filterIsInstance<FileSystem.File>().sumOf { it.size }

    fun FileSystem.findSmallestDirectoryAboveLimit(min: Int, limit: Int): Int {
        if (this is FileSystem.Dir) {
            val size = size()
            val currentMin = if (size in limit until min) size else min
            return content.minOf { it.findSmallestDirectoryAboveLimit(currentMin, limit) }
        }
        return min
    }

    fun part1(fs: FileSystem): Int {
        if (fs is FileSystem.Dir) {
            val filesSize = fs.walk().filterIsInstance<FileSystem.File>().sumOf { it.size }
            return (if (filesSize < 100000) filesSize else 0).plus(fs.content.sumOf { part1(it) })
        }
        return 0
    }

    fun part2(fs: FileSystem): Int {
        val limit = 30000000 - (70000000 - fs.size())
        return fs.findSmallestDirectoryAboveLimit(Int.MAX_VALUE, limit)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("Day07_test")
    val dayInput = readInputAsString("Day07")

    val testFs = parseFs(testInput)
    val dayFs = parseFs(dayInput)
    check(part1(testFs) == 95437)
    println(part1(dayFs))

    check(part2(testFs) == 24933642)
    println(part2(dayFs))
}
