import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun Position.manhattanDistance(other: Position) = abs(this.x - other.x) + abs(this.y - other.y)

@OptIn(ExperimentalTime::class)
fun main() {

    val inputLineRegex = Regex("""Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""")

    data class SensorBeaconArea(val sensor: Position, val beacon: Position) {
        val range = sensor.manhattanDistance(beacon)
        operator fun contains(position: Position) = position != beacon && sensor.manhattanDistance(position) <= range
    }

    fun parse(lines: List<String>): List<SensorBeaconArea> = lines.map { line ->
        val (sx, sy, bx, by) = inputLineRegex.matchEntire(line)!!.destructured
        SensorBeaconArea(
            (sx.toInt() to sy.toInt()),
            (bx.toInt() to by.toInt())
        )
    }

    fun yWithNoBeaconSpaceCount(y: Int, sensorsToBeacons: List<SensorBeaconArea>): Int {
        val xMin = sensorsToBeacons.minOf { sb -> sb.sensor.x - sb.range }
        val xMax = sensorsToBeacons.maxOf { sb -> sb.sensor.x + sb.range }
        return (xMin..xMax).count { x ->
            sensorsToBeacons.any { sensorBeaconArea ->
                val testedPosition = (x to y)
                testedPosition in sensorBeaconArea
            }
        }
    }

    fun part2(maxRange: Int, sensorsToBeacons: List<SensorBeaconArea>): Long {
        fun positionWithNoBeacon(maxRange: Int, sensorsToBeacons: List<SensorBeaconArea>): Position? {
            for (x in 0..maxRange) {
                var y = 0
                while (y <= maxRange) {
                    val testedPosition = (x to y)
                    val sensorBeaconArea = sensorsToBeacons.find { testedPosition in it } ?: return testedPosition
                    y = sensorBeaconArea.sensor.y + sensorBeaconArea.range - abs(x - sensorBeaconArea.sensor.x) + 1
                }
            }
            return null
        }

        val distressBeacon = requireNotNull(positionWithNoBeacon(maxRange, sensorsToBeacons))
        return distressBeacon.x.toLong() * 4000000L + distressBeacon.y.toLong()
    }

    val testInput = readInput("Day15_test")
    val input = readInput("Day15")

    val testSensorsToBeacons = parse(testInput)
    val sensorsToBeacons = parse(input)

    check(yWithNoBeaconSpaceCount(10, testSensorsToBeacons) == 26)
    measureTime {
        println(yWithNoBeaconSpaceCount(2000000, sensorsToBeacons))
    }.also { println("Time part1: $it") }

    check(part2(20, testSensorsToBeacons) == 56000011L)
    measureTime {
        println(part2(4000000, sensorsToBeacons))
    }.also { println("Time part2: $it") }

}
