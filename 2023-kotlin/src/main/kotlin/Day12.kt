/**
 *  For each row, the condition records show every spring and whether it is operational (.)
 *  or damaged (#). This is the part of the condition records that is itself damaged; for some springs,
 *  it is simply unknown (?) whether the spring is operational or damaged.
 */
enum class SpringCondition {
    OPERATIONAL, DAMAGED, UNKNOWN;

    companion object {
        fun fromChar(char: Char): SpringCondition {
            return when (char) {
                '.' -> OPERATIONAL
                '#' -> DAMAGED
                '?' -> UNKNOWN
                else -> throw Exception("Failed to parse unknown spring condition: $char")
            }
        }
    }
}

data class LavaRow(val row: List<SpringCondition>, val damageRecord: List<Int>) {
    companion object {
        // #.#.### 1,1,3
        fun fromString(line: String): LavaRow {
            val (springs, records) = line.split(" ")
            val parsedSprings = springs.map { SpringCondition.fromChar(it) }
            val parsedRecords = records.split(",").map { it.toInt() }
            return LavaRow(parsedSprings, parsedRecords)
        }
    }

    fun unfold(times: Int = 5): LavaRow {
        val unfoldedRow = List(times) { (row + listOf(SpringCondition.UNKNOWN)) }.flatten().dropLast(1)
        val unfoldedRecords = List(times) { damageRecord }.flatten()
        return LavaRow(unfoldedRow, unfoldedRecords)
    }

    override fun toString(): String {
        val springRepr = row.joinToString("") {
            when (it) {
                SpringCondition.DAMAGED -> "#"
                SpringCondition.UNKNOWN -> "?"
                SpringCondition.OPERATIONAL -> "."
            }
        }
        val recordsRepr = damageRecord.joinToString(",")
        return "$springRepr $recordsRepr"
    }
}

class Day12 : PuzzleSolution {
    override val input: List<String> = readInput("Day12")

    fun countPossibleArrangements(lavaRow: LavaRow): Long {
        // Transform the input so there is exactly one operational spring in the beginning of the row
        val springs: MutableList<SpringCondition> = (
                listOf(SpringCondition.OPERATIONAL)
                        + lavaRow.row.dropWhile { it == SpringCondition.OPERATIONAL }.toMutableList()
                ).toMutableList()

        /**
         * DP approach from https://www.ericburden.work/blog/2023/12/12/advent-of-code-day-12/
         */


        // First constraint: no groups of damaged part
        var possibleCombinations = MutableList(springs.size + 1) { 1L }

        // Starting from the first damaged part present in the input line, set the number of possible combinations to 0
        // as after that there will be no way of constructing a sequence with no damaged parts
        val firstDamageIdx = springs.indexOfFirst { it == SpringCondition.DAMAGED }
        if (firstDamageIdx >= 0) {
            (firstDamageIdx + 1..springs.size).forEach { idx ->
                possibleCombinations[idx] = 0
            }
        }

        // Build on the previous set of constraints for each consecutive group of damages
        lavaRow.damageRecord.forEach { damagedGroupSize ->
            val nextPossibilitiesLayer = MutableList(springs.size + 1) { 0L }
            var currentDamagedGroupSize = 0

            springs.withIndex().forEach { (partIdx, partCondition) ->
                // If we encounter '.' (operational part), reset the counter as we can start a new damage group
                if (partCondition == SpringCondition.OPERATIONAL) {
                    currentDamagedGroupSize = 0
                } else {
                    currentDamagedGroupSize += 1
                }

                /*
                 *     damage can fit = group size <= run of DAMAGED and UNKNOWN
                 *                    AND spring just prior to group start point is not DAMAGED
                 *       current[i] = if current section is not DAMAGED and damage can fit:
                 *           (A) current[i - 1] + (previous[i - (group size + 1)] ?: 0)
                 *       else if current section is DAMAGED and damage can fit:
                 *           (B) (previous[i - (group size + 1)] ?: 0)
                 *       else if current section is not DAMAGED:
                 *           (C) current[i - 1]
                 *       else:
                 *           (D) 0
                 */
                val canGroupBeIntroduced = damagedGroupSize <= currentDamagedGroupSize

                // Check the state of the last part before this damaged group to avoid continuing damage
                val precedingPartIdx = (partIdx - damagedGroupSize).coerceAtLeast(0)
                val isContinuousDamage = springs[precedingPartIdx] == SpringCondition.DAMAGED
                val canDamageBeIntroduced = canGroupBeIntroduced && !isContinuousDamage

                // Check the state of the current part
                val isNotDamaged = partCondition != SpringCondition.DAMAGED

                nextPossibilitiesLayer[partIdx + 1] =
                        if (isNotDamaged && canDamageBeIntroduced) {
                            nextPossibilitiesLayer[partIdx] + possibleCombinations[precedingPartIdx]
                        } else if (partCondition == SpringCondition.DAMAGED && canDamageBeIntroduced) {
                            possibleCombinations[precedingPartIdx]
                        } else if (isNotDamaged) {
                            nextPossibilitiesLayer[partIdx]
                        } else {
                            0L
                        }
            }
            possibleCombinations = nextPossibilitiesLayer
        }

        return possibleCombinations.last()
    }

    override fun part1(input: List<String>): String {
        val springRows = input.map { line -> LavaRow.fromString(line) }
        val possibilitiesCount = springRows.sumOf { countPossibleArrangements(it) }
        return possibilitiesCount.toString()
    }

    override fun part2(input: List<String>): String {
        val springRows = input.map { line -> LavaRow.fromString(line).unfold() }
        val possibilitiesCount = springRows.sumOf { countPossibleArrangements(it) }
        return possibilitiesCount.toString()
    }
}

fun main() {
    val solution = Day12()

    val testLavaRow1 = LavaRow.fromString("???.### 1,1,3")
    val testCount1 = solution.countPossibleArrangements(testLavaRow1)
    check(testCount1 == 1L) { "Expected 1, got $testCount1" }

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day12_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "21") { "Expected 21, got $testSolution1" }

    val unfoldedTestLavaRow2 = LavaRow.fromString(".# 1")
    val testUnfoldedTestLavaRow2 = unfoldedTestLavaRow2.unfold().toString()
    check(testUnfoldedTestLavaRow2 == ".#?.#?.#?.#?.# 1,1,1,1,1") { "Expected '.#?.#?.#?.#?.# 1,1,1,1,1', got '$testUnfoldedTestLavaRow2'" }

    val unfoldedTestLavaRow1 = testLavaRow1.unfold(5)
    val testCount2 = solution.countPossibleArrangements(unfoldedTestLavaRow1)
    check(testCount2 == 1L) { "Expected 1, got $testCount2" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "525152") { "Expected 525152, got $testSolution2" }

    solution.run()
}
