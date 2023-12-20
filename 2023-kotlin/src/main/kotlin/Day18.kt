import kotlin.math.abs

data class LavaDirection(val direction: String, val units: Long) {
    companion object {
        fun fromString(line: String): LavaDirection {
            val matches = Regex("(?<direction>\\w) (?<units>\\d+) .+").matchEntire(line)?.groups
            val direction = matches?.get("direction")?.value
            val units = matches?.get("units")?.value?.toLong()
            return LavaDirection(direction!!, units!!)
        }

        fun fromHexString(line: String): LavaDirection {
            val hexMatch = Regex("(\\w) (\\d+) \\(\\#(?<hex>.+)\\)").matchEntire(line)?.groups
            val hex = hexMatch?.get("hex")?.value!!
            // 0 means R, 1 means D, 2 means L, and 3 means U.
            val direction = when (hex.last()) {
                '0' -> "R"
                '1' -> "D"
                '2' -> "L"
                '3' -> "U"
                else -> throw IllegalArgumentException("Invalid hex digit: $hex")
            }
            val units = hex.dropLast(1).toLong(radix = 16)
            return LavaDirection(direction, units)
        }
    }
}

data class LavaCoord(val x: Long, val y: Long) {
    fun moveInDirection(direction: LavaDirection): LavaCoord {
        return when (direction.direction) {
            "R" -> LavaCoord(x, y + 1)
            "L" -> LavaCoord(x, y - 1)
            "U" -> LavaCoord(x - 1, y)
            "D" -> LavaCoord(x + 1, y)
            else -> throw IllegalArgumentException("Invalid direction: $direction")
        }
    }
}


data class Lagoon(val dugSpots: MutableList<LavaCoord>) {
    private fun dig(start: LavaCoord, direction: LavaDirection): LavaCoord {
        var current = start
        (1..direction.units).forEach { _ ->
            current = current.moveInDirection(direction)
            dugSpots.add(current)
        }
        return current
    }

    fun digAll(directions: List<LavaDirection>) {
        val start = LavaCoord(0, 0)
        var current = start
        directions.forEach { direction ->
            current = dig(current, direction)
        }
    }

    fun findShoelaceArea(): Long {
        val res = dugSpots.windowed(2).sumOf { (coord1, coord2) ->
            coord1.x * coord2.y - coord1.y * coord2.x
        }
        val area = abs(res) / 2
        return area + (dugSpots.size / 2L) + 1L
    }

    fun findShoelaceAreaDynamic(): Long {
        var totalArea = 0L
        for (offset in 1 until dugSpots.size) {
            val coord1 = dugSpots[offset]
            val coord2 = dugSpots[(offset + 1) % dugSpots.size]
            val area = coord1.x * coord2.y - coord1.y * coord2.x
            totalArea += area
        }
        totalArea = abs(totalArea) / 2L
        return totalArea + (dugSpots.size / 2L) + 1L
    }

}

class Day18 : PuzzleSolution {
    override val input: List<String> = readInput("Day18")
    override fun part1(input: List<String>): String {
        val directions = input.map { LavaDirection.fromString(it) }
        val lagoon = Lagoon(mutableListOf())
        lagoon.digAll(directions)

        return lagoon.findShoelaceArea().toString()
    }

    override fun part2(input: List<String>): String {
        val directions = input.map { LavaDirection.fromHexString(it) }
        val lagoon = Lagoon(mutableListOf())
        lagoon.digAll(directions)

        return lagoon.findShoelaceAreaDynamic().toString()
    }
}

fun main() {
    val solution = Day18()
    val testInput = solution.readInput("Day18_test")

    // test if implementation meets criteria from the description, like:
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "62") { "Expected 62, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2.toString() == "952408144115") { "Expected 952408144115, got $testSolution2" }

    solution.run()
}
