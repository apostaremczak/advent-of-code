enum class NetworkInstruction {
    LEFT, RIGHT
}

data class NetworkNode(val name: String, val leftName: String, val rightName: String) {
    companion object {
        /**
         * "AAA = (BBB, CCC)" => NetworkNode(AAA, BBB, CCC)
         */
        fun fromString(line: String): NetworkNode {
            val matches = Regex("(?<name>\\w+) = \\((?<left>\\w+), (?<right>\\w+)\\)").matchEntire(line)?.groups
            val name = matches?.get("name")?.value!!
            val left = matches["left"]?.value!!
            val right = matches["right"]?.value!!
            return NetworkNode(name, left, right)
        }
    }
}

class Network(val instructions: List<NetworkInstruction>, val nodeConnections: Map<String, NetworkNode>) {
    companion object {
        fun fromPuzzleInput(input: String): Network {
            val (instrDefs, nodeDefs) = input.split("\n\n")
            val instructions = instrDefs.mapNotNull { instr ->
                when (instr) {
                    'L' -> NetworkInstruction.LEFT
                    'R' -> NetworkInstruction.RIGHT
                    else -> null
                }
            }
            check(instrDefs.length == instructions.size) { "Failed to parse instructions from $instrDefs" }

            val nodeConnDefs = nodeDefs.split("\n").filter { it.isNotEmpty() }.associate { line ->
                val node = NetworkNode.fromString(line)
                node.name to node
            }

            return Network(instructions, nodeConnDefs)
        }
    }

    private tailrec fun findPathLength(
            currentNode: NetworkNode,
            currentInstructionIdx: Int = 0,
            currentPathLength: Int = 0,
            destinationNodeName: String = "ZZZ"
    ): Int {
        if (currentNode.name == destinationNodeName) {
            return currentPathLength
        }

        val instr = instructions[currentInstructionIdx]
        val nextNode = when (instr) {
            NetworkInstruction.LEFT -> nodeConnections[currentNode.leftName]
            NetworkInstruction.RIGHT -> nodeConnections[currentNode.rightName]
        }
        val nextInstructionIdx = (currentInstructionIdx + 1) % instructions.size

        return findPathLength(nextNode!!, nextInstructionIdx, currentPathLength + 1, destinationNodeName)
    }

    fun findPathLength(startNodeName: String = "AAA", destinationNodeName: String = "ZZZ"): Int {
        return findPathLength(
                currentNode = nodeConnections[startNodeName]!!,
                destinationNodeName = destinationNodeName
        )
    }
}

class Day08 : PuzzleSolutionNonSplitInput {
    override val input: String = readInputNotSplit("Day08")
    override fun part1(input: String): String {
        val network = Network.fromPuzzleInput(input)
        return network.findPathLength().toString()
    }

    override fun part2(input: String): String {
        val network = Network.fromPuzzleInput(input)
        return "Not implemented"
    }
}

fun main() {
    val solution = Day08()

    // test if implementation meets criteria from the description, like:
    val testInput1 = solution.readInputNotSplit("Day08_test1")
    val testSolution1 = solution.part1(testInput1)
    check(testSolution1 == "2") { "Expected 2, got $testSolution1" }

    val testInput2 = solution.readInputNotSplit("Day08_test2")
    val testSolution2 = solution.part1(testInput2)
    check(testSolution2 == "6") { "Expected 6, got $testSolution2" }

    val testInput3 = solution.readInputNotSplit("Day08_test3")
    val testSolution3 = solution.part2(testInput3)
//    check(testSolution3 == "6") { "Expected 6, got $testSolution3" }

    solution.run()
}
