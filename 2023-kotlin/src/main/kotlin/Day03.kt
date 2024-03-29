data class Coord(val row: Int, val col: Int)

data class Number(val value: Int, val occupiedCells: Set<Coord>)

data class EngineSchematic(
    val symbolCoords: Map<Coord, Char>,
    val numbers: Set<Number>,
    private val cellsOccupiedByNumbers: Map<Coord, Number>
) {
    private val allDiffs = listOf(-1, 0, 1)
        .flatMap { rowDiff ->
            listOf(-1, 0, 1).map { colDiff ->
                Pair(rowDiff, colDiff)
            }
        }
        .filterNot { it == 0 to 0 }

    companion object {
        fun fromStringMap(schematicStr: List<String>): EngineSchematic {
            val symbolCoords: MutableMap<Coord, Char> = mutableMapOf()
            val numbers: MutableSet<Number> = mutableSetOf()

            schematicStr.forEachIndexed { rowIdx, line ->
                // Find all numbers
                val numbersInTheLine = Regex("(\\d+)").findAll(line).map { res ->
                    val occupiedCells = (res.range.first..res.range.last).map { colIdx ->
                        Coord(rowIdx, colIdx)
                    }
                    Number(res.value.toInt(), occupiedCells.toSet())
                }
                numbers.addAll(numbersInTheLine)

                // Find all symbols
                Regex("([^.\\d])").findAll(line).forEach { res ->
                    val symbolCoord = Coord(rowIdx, res.range.first)
                    symbolCoords[symbolCoord] = res.value.single()
                }
            }

            val cellsOccupiedByNumbers: Map<Coord, Number> = numbers.flatMap { number ->
                number.occupiedCells.map { coord ->
                    coord to number
                }
            }.toMap()

            return EngineSchematic(symbolCoords.toMap(), numbers.toSet(), cellsOccupiedByNumbers)
        }
    }

    fun getNumbersAdjacentToSymbol(symbolCoord: Coord): Set<Number> {
        return allDiffs.mapNotNull { (rowDiff, colDiff) ->
            val cellCoords = Coord(symbolCoord.row + rowDiff, symbolCoord.col + colDiff)
            cellsOccupiedByNumbers[cellCoords]
        }.toSet()
    }

    fun getGearRatios(): List<Int> {
        return symbolCoords.filter { (_, symbol) -> symbol == '*' }.mapNotNull { (coord, _) ->
            val adjacent = getNumbersAdjacentToSymbol(coord)
            if (adjacent.size == 2) {
                adjacent.map { it.value }.reduce(Int::times)
            } else null
        }
    }
}


class Day3 : PuzzleSolution {
    override val input: List<String> = readInput("Day03")
    override fun part1(input: List<String>): String {
        val schematic = EngineSchematic.fromStringMap(input)
        val allAdjacentNumbers: Set<Number> = schematic.symbolCoords.flatMap { (symbolCoord, _) ->
            schematic.getNumbersAdjacentToSymbol(symbolCoord)
        }.toSet()

        return allAdjacentNumbers.sumOf { it.value }.toString()
    }

    override fun part2(input: List<String>): String {
        val schematic = EngineSchematic.fromStringMap(input)
        val gears = schematic.getGearRatios()

        return gears.sum().toString()
    }
}

fun main() {
    val solution = Day3()

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day03_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "4361") { "Expected 4361, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "467835") { "Expected 467835, got $testSolution2" }

    solution.run()
}
