class DayX : PuzzleSolution {
    override val input: List<String> = readInput("DayX")
    override fun part1(input: List<String>): String {
        return "Not implemented"
    }
    override fun part2(input: List<String>): String {
        return "Not implemented"
    }
}

fun main() {
    val solution = DayX()

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("DayX_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "13") { "Expected 13, got $testSolution1" }

//    val testSolution2 = solution.part2(testInput)
//    check(testSolution2 == "30") { "Expected 30, got $testSolution2" }

    solution.run()
}
