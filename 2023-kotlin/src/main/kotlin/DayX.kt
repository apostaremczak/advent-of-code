class DayX : PuzzleSolution {
    override val input: List<String> = readInput("DayX")
    override fun part1(input: List<String>): Int {
        return input.size
    }
    override fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val solution = DayX()

    // test if implementation meets criteria from the description, like:
    val testInput1 = solution.readInput("DayX_test1")
    val testSolution1 = solution.part1(testInput1)
    check(testSolution1 == 0) { "Expected 0, got $testSolution1" }

//    val testInput2 = solution.readInput("DayX_test2")
//    check(solution.part2(testInput2) == 281)

    solution.run()
}
