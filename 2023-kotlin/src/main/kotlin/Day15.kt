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

data class Lens(val label: String, var focalLength: Int?) {
    val boxNumber: Int = label.aocHash()
}

class Day15 : PuzzleSolutionNonSplitInput {
    override val input: String = readInputNotSplit("Day15").replace("\n", "")

    override fun part1(input: String): String {
        val components = input.split(",")
        return components.sumOf { it.aocHash() }.toString()
    }

    override fun part2(input: String): String {
        val boxes: List<MutableList<Lens>> = List(256) { mutableListOf<Lens>() }
        val components = input.split(",")

        components.forEach { component ->
            if (component.contains("=")) {
                val lens = Lens(component.substringBefore("="), component.substringAfter("=").toInt())
                // If the lens already exists in the box, update the focal length
                if (boxes[lens.boxNumber].any { it.label == lens.label }) {
                    boxes[lens.boxNumber].find { it.label == lens.label }?.focalLength = lens.focalLength
                } else {
                    boxes[lens.boxNumber].add(lens)
                }

            }
            if (component.contains("-")) {
                val lensLabel = component.substringBefore("-")
                val lensBoxNumber = lensLabel.aocHash()
                boxes[lensBoxNumber].removeIf { it.label == lensLabel }
            }
        }

        /**
         *     One plus the box number of the lens in question.
         *     The slot number of the lens within the box: 1 for the first lens, 2 for the second lens, and so on.
         *     The focal length of the lens.
         */
        return boxes.withIndex().sumOf { (boxIdx, box) ->
            box.withIndex().sumOf { (lensIdx, lens) ->
                (boxIdx + 1) * (lensIdx + 1) * lens.focalLength!!
            }
        }.toString()
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

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "145") { "Expected 145, got $testSolution2" }

    solution.run()
}
