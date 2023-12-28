data class UndirectedEdge(val start: String, val end: String)

data class MinCutResult(val removedEdges: Int, val firstPartCount: Int, val secondPartCount: Int)

class UndirectedGraph(private val edges: List<UndirectedEdge>) {
    private val nodesCount: Int = edges.flatMap { listOf(it.start, it.end) }.toSet().size

    /**
     * Karger’s algorithm (source: https://www.cs.princeton.edu/courses/archive/fall13/cos521/lecnotes/lec2final.pdf)
     * Here’s a randomized algorithm for finding the minimum cut:
     * - Repeat until just two nodes remain:
     *     - Pick an edge of G at random and collapse its two endpoints into a single node
     * - For the two remaining nodes u_1 and u_2,
     *     - set V_1 = {nodes that went into u_1 }
     *     - set V_2 = {nodes in u_2 }
     *
     * Returns:
     *  - Triple (cut_count, V_1, V_2) representing the minimum cut
     */
    fun minCut(): MinCutResult {
        // Make a copy of the edges to avoid concurrent modification
        var collapsedEdges = edges
        var collapsedNodesCount = nodesCount
        // Keep a track of collapsed nodes
        val collapsed: MutableMap<String, MutableList<String>> = mutableMapOf()

        while (collapsedNodesCount > 2) {
            // Pick a random edge
            val edgeToCollapse = collapsedEdges.random()

            // Collapse: mark the start and point to the collapsed edge
            collapsed.computeIfAbsent(edgeToCollapse.start) { mutableListOf() }.add(edgeToCollapse.end)

            // If the end of this edge is already in the collapsed cache, link it to the new start
            if (collapsed.containsKey(edgeToCollapse.end)) {
                collapsed[edgeToCollapse.start]!!.addAll(collapsed[edgeToCollapse.end]!!)
                collapsed.remove(edgeToCollapse.end)
            }

            val newEdges = collapsedEdges.map { edge ->
                if (edge.end == edgeToCollapse.end) {
                    UndirectedEdge(edge.start, edgeToCollapse.start)
                } else if (edge.start == edgeToCollapse.end) {
                    UndirectedEdge(edgeToCollapse.start, edge.end)
                } else {
                    edge
                }
            }

            // Remove self-loops
            collapsedEdges = newEdges.filter { it.start != it.end }
            collapsedNodesCount--
        }

        // To each group size, add the first node
        val partCounts = collapsed.map { it.value.size + 1 }

        return MinCutResult(collapsedEdges.size, partCounts.first(), partCounts.last())
    }

    /**
     * Returns graph representation for GraphViz
     */
    override fun toString(): String {
        val distinctEdgeConnections = edges.distinct()
        var str = ""
        distinctEdgeConnections.forEach { edge ->
            str += "${edge.start} -- ${edge.end}\n"
        }
        return str
    }
}

class Day25 : PuzzleSolution {
    override val input: List<String> = readInput("Day25")
    override fun part1(input: List<String>): String {
        val edges = input.flatMap { line ->
            val from = line.substringBefore(": ")
            val destinations = line.substringAfter(" ").split(" ")
            destinations.map { to -> UndirectedEdge(from, to) }
        }
        val graph = UndirectedGraph(edges)

        var minCutResult = MinCutResult(Int.MAX_VALUE, 0, 0)
        while (minCutResult.removedEdges != 3) {
            minCutResult = graph.minCut()
        }

        return (minCutResult.firstPartCount * minCutResult.secondPartCount).toString()
    }

    override fun part2(input: List<String>): String {
        return "Still missing some stars"
    }
}

fun main() {
    val solution = Day25()

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day25_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "54") { "Expected 54, got $testSolution1" }

    solution.run()
}
