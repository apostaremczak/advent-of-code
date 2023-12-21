data class Tile(val row: Int, val col: Int) {
    fun surroundingTiles(): Set<Tile> {
        return setOf(
                Tile(row - 1, col),
                Tile(row + 1, col),
                Tile(row, col - 1),
                Tile(row, col + 1)
        )
    }
}

data class GardenMap(var currentlyOccupiedTiles: Set<Tile>, val walls: Set<Tile>, val maxRow: Int, val maxCol: Int) {
    companion object {
        fun fromInput(input: List<String>): GardenMap {
            var startingPointTile: Tile? = null
            val walls: Set<Tile> = input.withIndex().flatMap { (rowIdx, row) ->
                row.withIndex().mapNotNull { (colIdx, c) ->
                    when (c) {
                        '#' -> Tile(rowIdx, colIdx)
                        'S' -> {
                            startingPointTile = Tile(rowIdx, colIdx); null
                        }

                        else -> null
                    }
                }
            }.toSet()
            return GardenMap(
                    setOf(startingPointTile!!),
                    walls,
                    input.maxOfOrNull { it.length } ?: 0,
                    input.firstOrNull()?.length ?: 0
            )
        }
    }

    private fun takeStep() {
        val nextPossibleSteps: Set<Tile> = currentlyOccupiedTiles.flatMap { it.surroundingTiles() }
                .filter { it.col in 0 until maxCol && it.row in 0 until maxRow }.toSet()
                .minus(walls)
        currentlyOccupiedTiles = nextPossibleSteps
    }

    fun takeSteps(numSteps: Int) {
        repeat(numSteps) {
            takeStep()
        }
    }

    fun println() {
        for (row in 0 until maxRow) {
            for (col in 0 until maxCol) {
                val t = Tile(row, col)
                if (currentlyOccupiedTiles.contains(t)) {
                    print("O")
                } else if (walls.contains(t)) {
                    print("#")
                } else {
                    print(".")
                }
            }
            print("\n")
        }
    }

    private fun takeStepOnInfiniteMap() {
//        val nextPossibleSteps: Set<Tile> = currentlyOccupiedTiles.flatMap { it.surroundingTiles() }
//                .filter { it.col in 0 until maxCol && it.row in 0 until maxRow }.toSet()
//                .minus(walls)
//        currentlyOccupiedTiles = nextPossibleSteps
    }

    fun takeStepsOnInfiniteMap(numSteps: Int) {
        repeat(numSteps) {
            takeStepOnInfiniteMap()
        }
    }
}

class Day21 : PuzzleSolution {
    override val input: List<String> = readInput("Day21")


    override fun part1(input: List<String>): String {
        val gardenMap = GardenMap.fromInput(input)
        gardenMap.takeSteps(64)
        return gardenMap.currentlyOccupiedTiles.size.toString()
    }

    override fun part2(input: List<String>): String {
        val gardenMap = GardenMap.fromInput(input)
        gardenMap.takeStepsOnInfiniteMap(26501365)
        return gardenMap.currentlyOccupiedTiles.size.toString()
    }
}

fun main() {
    val solution = Day21()

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day21_test")
    val testGardenMap = GardenMap.fromInput(testInput)
    testGardenMap.takeSteps(6)
    val testSolution1 = testGardenMap.currentlyOccupiedTiles.size.toString()
    check(testSolution1 == "16") { "Expected 16, got $testSolution1" }

//    val testSolution2 = solution.part2(testInput)
//    check(testSolution2 == "30") { "Expected 30, got $testSolution2" }

    solution.run()
}
