class Day01 : PuzzleSolution {
    override val input: List<String> = readInput("Day01")

    override fun part1(input: List<String>): String {
        val codes: List<Int> = input.map { line ->
            val digits: List<Char> = line.toList().filter { it.isDigit() }
            (digits.first().toString() + digits.last().toString()).toInt()
        }
        return codes.sum().toString()
    }


    private val digitsSpelled = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
            .withIndex()
            .associate { it.value to it.index.toString() }

    fun parseLine(line: String): Int {
        val writtenDigitsIdx: List<Pair<Int, String>> = digitsSpelled
                .flatMap { (key, value) ->
                    val allDigitIndices = Regex(key).findAll(line).map { it.range.first }
                    allDigitIndices.map { Pair(it, value) }
                }
                .filter { (idx, _) -> idx >= 0 }

        val firstWrittenDigit = writtenDigitsIdx.minByOrNull { (idx, _) -> idx }
        val firstWrittenDigitIdx = firstWrittenDigit?.first ?: Int.MAX_VALUE
        val lastWrittenDigit = writtenDigitsIdx.maxByOrNull { (idx, _) -> idx }
        val lastWrittenDigitIdx = lastWrittenDigit?.first ?: Int.MIN_VALUE

        val digits: List<Char> = line.toList().filter { it.isDigit() }
        val firstDigit = digits.firstOrNull()
        val firstDigitIdx = firstDigit?.let { line.indexOf(it) } ?: Int.MAX_VALUE
        val lastDigit = digits.lastOrNull()
        val lastDigitIdx = lastDigit?.let { line.lastIndexOf(it) } ?: Int.MIN_VALUE

        val first: String = if (firstWrittenDigitIdx < firstDigitIdx) {
            firstWrittenDigit?.second ?: ""
        } else {
            line[firstDigitIdx].toString()
        }

        val second = if (lastWrittenDigitIdx > lastDigitIdx) {
            lastWrittenDigit?.second ?: ""
        } else {
            line[lastDigitIdx].toString()
        }

        return (first + second).toInt()
    }

    override fun part2(input: List<String>): String {
        val codes: List<Int> = input.map { parseLine(it) }
        return codes.sum().toString()
    }
}

fun main() {
    val solution = Day01()

    // test if implementation meets criteria from the description, like:
    val testInput1 = solution.readInput("Day01_test1")
    check(solution.part1(testInput1) == "142")

    val testInput2 = solution.readInput("Day01_test2")
    check(solution.part2(testInput2) == "281")

    check(solution.parseLine("2xjzgsjzfhzhm1") == 21)
    check(solution.parseLine("kvbhsseven2rkbllhrhvsevenfour2vf") == 72) { "Got ${solution.parseLine("kvbhsseven2rkbllhrhvsevenfour2vf")}" }
    check(solution.parseLine("29oneightt") == 28) { "Got ${solution.parseLine("29oneightt")}" }

    solution.run()
}
