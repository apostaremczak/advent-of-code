import kotlin.math.pow

data class Scratchcard(val id: Int, val winning: Set<Int>, val owned: Set<Int>) {
    private val ownedWinning = winning.intersect(owned)
    val wonDuplicates: List<Int> = (1..ownedWinning.size).map { id + it }

    fun getScore(): Int {
        val ownedWinningCount = ownedWinning.size
        return when {
            ownedWinningCount == 0 -> 0
            else -> 2.0.pow(ownedWinningCount - 1).toInt()
        }
    }
}

class Day04 : PuzzleSolution {
    override val input: List<String> = readInput("Day04")

    private fun parseNumberLine(numbers: String): Set<Int> {
        val numMatches = Regex("(\\d+)").findAll(numbers)
        return numMatches.filter { it.value.isNotEmpty() }.map { it.value.toInt() }.toSet()
    }

    private fun parseCard(line: String): Scratchcard {
        val matched = Regex("Card\\s+(?<cardId>\\d+)+: (?<winning>([\\d\\s])+)\\|(?<owned>[\\d\\s]+)")
            .matchEntire(line)?.groups
        val cardId = matched?.get("cardId")?.value!!
        val winningStr = matched["winning"]?.value!!
        val ownedStr = matched["owned"]?.value!!
        return Scratchcard(cardId.toInt(), parseNumberLine(winningStr), parseNumberLine(ownedStr))
    }

    override fun part1(input: List<String>): String {
        return input.sumOf { cardStr ->
            parseCard(cardStr).getScore()
        }.toString()
    }

    override fun part2(input: List<String>): String {
        val cardMappings = input.associate {
            val card = parseCard(it)
            card.id to card.wonDuplicates
        }

        val cardCopiesCount: MutableMap<Int, Int> = cardMappings.keys.associateWith { 1 }.toMutableMap()

        cardMappings.forEach { (cardId, wonDuplicates) ->
            val cardMultiplier = cardCopiesCount[cardId]!!
            wonDuplicates.forEach { wonDuplicateId ->
                cardCopiesCount[wonDuplicateId] = cardCopiesCount[wonDuplicateId]!! + cardMultiplier
            }
        }

        return cardCopiesCount.values.sum().toString()
    }
}

fun main() {
    val solution = Day04()

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day04_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "13") { "Expected 13, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "30") { "Expected 30, got $testSolution2" }

    solution.run()
}
