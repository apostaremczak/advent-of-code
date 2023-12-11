import kotlin.math.abs

data class GalaxyCoord(val x: Long, val y: Long) {
    fun distanceFrom(other: GalaxyCoord): Long {
     return abs(x - other.x) + abs(y - other.y)
    }
}

data class Galaxy(val occupiedCoords: Set<GalaxyCoord>) {
    fun expand(factor: Int = 2): Galaxy {
        // Find non-occupied rows and columns
        val nonOccupiedXs = (0..occupiedCoords.maxOf { it.x }).toSet() - occupiedCoords.map { it.x }.toSet()
        val nonOccupiedYs = (0..occupiedCoords.maxOf { it.y }).toSet() - occupiedCoords.map { it.y }.toSet()

        val xRangeBoundaries = (nonOccupiedXs + setOf(0)).toList().sorted()
        val yRangeBoundaries = (nonOccupiedYs + setOf(0)).toList().sorted()

        // For each non-occupied row and column, multiply the amount of unoccupied distance
        fun modify(coord: GalaxyCoord): GalaxyCoord {
            val xIncrease = xRangeBoundaries.indexOfLast { coord.x >= it } * (factor - 1)
            val yIncrease = yRangeBoundaries.indexOfLast { coord.y >= it } * (factor - 1)
            return GalaxyCoord(coord.x + xIncrease, coord.y + yIncrease)
        }

        val modifiedCoords = occupiedCoords.map { modify(it) }.toSet()
        return Galaxy(modifiedCoords)
    }

    private fun findAllTwoPairs(): Set<Set<GalaxyCoord>> {
        return occupiedCoords.flatMap { c1 ->
            occupiedCoords.filter { c2 -> c1 != c2 }.map { c2 -> setOf(c1, c2) }
        }.toSet()
    }

    fun getDistancesSum(): Long {
        val pairs = findAllTwoPairs()
        val distances = pairs.map { pair ->
            val (c1, c2) = pair.toList()
            c1.distanceFrom(c2)
        }
        return distances.sum()
    }

    companion object {
        fun fromString(input: List<String>): Galaxy {
            val occupiedCoords: Set<GalaxyCoord> = input.withIndex().flatMap { (x, row) ->
                row.withIndex().mapNotNull { (y, char) ->
                    if (char == '#') GalaxyCoord(x.toLong(), y.toLong()) else null
                }
            }.toSet()

            return Galaxy(occupiedCoords)
        }
    }
}

class Day11 : PuzzleSolution {
    override val input: List<String> = readInput("Day11")

    override fun part1(input: List<String>): Int {
        val galaxy = Galaxy.fromString(input).expand()
        return galaxy.getDistancesSum().toInt()
    }

    override fun part2(input: List<String>): Int {
        val galaxy = Galaxy.fromString(input).expand(factor = 1000000)
        val distanceSum = galaxy.getDistancesSum()
        if (distanceSum > Int.MAX_VALUE) {
            println("Answer too large for Int")
            println(distanceSum)
            return -1
        }
        return distanceSum.toInt()
    }
}

fun main() {
    val solution = Day11()

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day11_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == 374) { "Expected 374, got $testSolution1" }

    // Test expanding galaxies with different factors
    val testGalaxy = Galaxy.fromString(testInput)

    val testExpansionBy2 = testGalaxy.expand(2).getDistancesSum()
    check(testExpansionBy2 == 374L) { "Expected 374, got $testExpansionBy2" }

    val testExpansionBy10 = testGalaxy.expand(10).getDistancesSum()
    check(testExpansionBy10 == 1030L) { "Expected 1030, got $testExpansionBy10" }

    val testExpansionBy100 = testGalaxy.expand(100).getDistancesSum()
    check(testExpansionBy100 == 8410L) { "Expected 8410, got $testExpansionBy100" }

    solution.run()
}
