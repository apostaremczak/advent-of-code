/**
 *     Determine the ASCII code for the current character of the string.
 *     Increase the current value by the ASCII code you just determined.
 *     Set the current value to itself multiplied by 17.
 *     Set the current value to the remainder of dividing itself by 256.
 */
fun String.aocHash(): Int {
    var hashValue = 0
    this.forEach { char ->
        hashValue += char.code
        hashValue *= 17
        hashValue = hashValue.mod(256)
    }
    return hashValue
}

class Day15 : PuzzleSolutionNonSplitInput {
    override val input: String = readInputNotSplit("Day15").replace("\n", "")
    override fun part1(input: String): String {
        val components = input.split(",")
        return components.sumOf { it.aocHash() }.toString()
    }
    override fun part2(input: String): String {
        return "Not implemented"
    }
}

fun main() {
    val solution = Day15()

    check("HASH".aocHash() == 52)
    check("rn=1".aocHash() == 30)

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInputNotSplit("Day15_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "1320") { "Expected 1320, got $testSolution1" }

//    val testSolution2 = solution.part2(testInput)
//    check(testSolution2 == "30") { "Expected 30, got $testSolution2" }

    solution.run()
}
