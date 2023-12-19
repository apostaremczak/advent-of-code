data class Sequence(val elements: List<Int>) {
    val differences = elements.windowed(2).map { (first, second) -> second - first }

    companion object {
        fun fromString(line: String): Sequence {
            val digits = Regex("([\\-\\d]+)").findAll(line).map { it.value.toInt() }.toList()
            return Sequence(digits)
        }
    }

    fun extrapolate(): Int {
        if (differences.toSet() == setOf(0) || elements.toSet().size == 1) {
            return elements.last()
        }
        return elements.last() + Sequence(differences).extrapolate()
    }

    fun extrapolateBackwards(): Int {
        if (differences.toSet() == setOf(0) || elements.toSet().size == 1) {
            return elements.first()
        }
        return elements.first() - Sequence(differences).extrapolateBackwards()
    }
}

class Day09 : PuzzleSolution {
    override val input: List<String> = readInput("Day09")
    override fun part1(input: List<String>): String {
        val seqs = input.map { Sequence.fromString(it) }
        val nextTermsSum = seqs.sumOf { it.extrapolate().toLong() }
        return nextTermsSum.toInt().toString()
    }

    override fun part2(input: List<String>): String {
        val seqs = input.map { Sequence.fromString(it) }
        val previousTermsSum = seqs.sumOf { it.extrapolateBackwards().toLong() }
        return previousTermsSum.toString()
    }
}

fun main() {
    val solution = Day09()

    // Test Sequence parsing
    val testSeq1 = Sequence.fromString("0   3   6   9  12  15")
    check(testSeq1.elements == listOf(0, 3, 6, 9, 12, 15))
    check(testSeq1.differences == listOf(3, 3, 3, 3, 3))
    check(testSeq1.extrapolate() == 18)

    val testSeq2 = Sequence(listOf(10, 13, 16, 21, 30, 45))
    check(testSeq2.differences == listOf(3, 3, 5, 9, 15))
    check(testSeq2.extrapolate() == 68)

    val testSeq3 = Sequence.fromString("-5 -3")
    check(testSeq3.elements == listOf(-5, -3))

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day09_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "114") { "Expected 114, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "2") { "Expected 2, got $testSolution2" }

    solution.run()
}
