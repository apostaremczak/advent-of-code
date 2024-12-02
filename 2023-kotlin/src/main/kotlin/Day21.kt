data class Tile(val row: Int, val col: Int) {
    fun surroundingTiles(): Set<Tile> {
        return setOf(
                Tile(row - 1, col),
                Tile(row + 1, col),
                Tile(row, col - 1),
                Tile(row, col + 1)
        )
    }

    fun intersectsWall(wallTile: Tile, maxMapRow: Int, maxMapCol: Int): Boolean {
        // Check walls infinitely, i.e. wrap the map around when the tile is outside the original bounds
        val wrappedRow = row.mod(maxMapRow)
        val wrappedCol = col.mod(maxMapCol)
        return wallTile == Tile(wrappedRow, wrappedCol)
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
            println("Step ${it + 1}, ${currentlyOccupiedTiles.size} tiles occupied")
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
        val nextPossibleSteps = currentlyOccupiedTiles.flatMap { it.surroundingTiles() }
                .filter { tile -> !walls.any { wall -> tile.intersectsWall(wall, maxRow, maxCol) } }
                .toSet()

        currentlyOccupiedTiles = nextPossibleSteps
    }

    fun takeStepsOnInfiniteMap(numSteps: Int) {
        val occupiedAtSteps = mutableListOf(currentlyOccupiedTiles.size)
        repeat(numSteps) {
//            var prev = currentlyOccupiedTiles.size
            takeStep()
//            println("Step ${it + 1}, ${currentlyOccupiedTiles.size} tiles occupied; increase: ${currentlyOccupiedTiles.size - prev}")
            occupiedAtSteps.add(currentlyOccupiedTiles.size)
        }
        println(occupiedAtSteps)
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

//    fun findReachableTiles(startingTile: Tile, numSteps: Int): Set<Tile> {
//        val reachableTiles: MutableSet<Tile> = mutableSetOf()
//    }
}

fun main() {
    val solution = Day21()

    // Test wrapping tiles around indefinitely
    val testTile1 = Tile(5, -2)
    val testWall1 = Tile (5, 9)
    check(testTile1.intersectsWall(testWall1, 11, 11)) {"Tiles should intersect"}

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day21_test")
//    val testGardenMap1 = GardenMap.fromInput(testInput)
//    testGardenMap1.takeSteps(6)
//    val testSolution1 = testGardenMap1.currentlyOccupiedTiles.size.toString()
//    check(testSolution1 == "16") { "Expected 16, got $testSolution1" }
//
//    val testGardenMap2 = GardenMap.fromInput(testInput)
//    testGardenMap2.takeStepsOnInfiniteMap(50)
//    val testSolution2 = testGardenMap2.currentlyOccupiedTiles.size.toString()
//    check(testSolution2 == "1594") { "Expected 1594, got $testSolution2" }
//
//    val testGardenMap3 = GardenMap.fromInput(testInput)
//    testGardenMap3.takeStepsOnInfiniteMap(100)
//    val testSolution3 = testGardenMap3.currentlyOccupiedTiles.size.toString()
//    check(testSolution3 == "6536") { "Expected 6536, got $testSolution3" }

//    val testGardenMap4 = GardenMap.fromInput(testInput)
//    testGardenMap4.takeStepsOnInfiniteMap(500)
//    val testSolution4 = testGardenMap4.currentlyOccupiedTiles.size.toString()
//    check(testSolution4 == "167004") { "Expected 167004, got $testSolution4" }

    solution.run()

    // Solution for part 2: 625587097150084
}
