import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt


data class BoatRace(val totalTime: Double, val record: Double) {
    fun getWinningRangeLength(): Int {
        val deltaSqrt = sqrt(totalTime.pow(2) - 4 * record)
        val rangeStart = floor(0.5 * (totalTime - deltaSqrt) + 1).toInt()
        val rangeEnd = ceil(0.5 * (totalTime + deltaSqrt) - 1).toInt()
        return rangeEnd - rangeStart + 1
    }
}

class Day06 : PuzzleSolution {
    override val input: List<String> = readInput("Day06")

    private fun getNums(line: String): List<Double> {
        return Regex("\\d+").findAll(line).map { it.value.toDouble() }.toList()
    }

    override fun part1(input: List<String>): Int {
        val totalTimes = getNums(input.first())
        val recordDistances = getNums(input.last())
        val boatRaces = totalTimes.zip(recordDistances).map { (totalTime, record) ->
            BoatRace(totalTime, record)
        }
        val waysToBeat = boatRaces.map { it.getWinningRangeLength() }
        return waysToBeat.reduce(Int::times)
    }

    private fun getNumsConcatenated(line: String): Double {
        return Regex("\\d+").findAll(line).map { it.value }.reduce(String::plus).toDouble()
    }

    override fun part2(input: List<String>): Int {
        val totalTime = getNumsConcatenated(input.first())
        val recordDistance = getNumsConcatenated(input.last())
        val boatRace = BoatRace(totalTime, recordDistance)
        return boatRace.getWinningRangeLength()
    }
}

fun main() {
    val solution = Day06()

    val testBoatRace = BoatRace(15.0, 40.0)
    check(testBoatRace.getWinningRangeLength() == 8)

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day06_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == 288) { "Expected 288, got $testSolution1" }


    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == 71503) { "Expected 71503, got $testSolution2" }

    solution.run()
}
