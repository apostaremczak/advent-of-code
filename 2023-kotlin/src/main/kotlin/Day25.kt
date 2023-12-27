data class ElectricComponent(val name: String) {
    private val connectedTo: MutableSet<ElectricComponent> = mutableSetOf()

    fun connectTo(component: ElectricComponent) {
        connectedTo.add(component)
        component.connectedTo.add(this)
    }
}

// Two element sets are used as undirected edges between components
typealias ElectricEdge = Set<String>

data class CollapsedElectricNode(val nodes: Set<String>) {
    companion object {
        fun fromSingleNode(nodeName: String): CollapsedElectricNode = CollapsedElectricNode(setOf(nodeName))
    }

    fun collapseWith(other: CollapsedElectricNode): CollapsedElectricNode {
        return CollapsedElectricNode(nodes + other.nodes)
    }

    override fun toString(): String {
        return nodes.sorted().joinToString(", ")
    }
}

class CollapsedElectricEdge(val start: CollapsedElectricNode, val end: CollapsedElectricNode) {
    companion object {
        fun fromSingleEdge(edge: Set<String>): CollapsedElectricEdge {
            val (start, end) = edge.toList()
            return CollapsedElectricEdge(
                    CollapsedElectricNode.fromSingleNode(start),
                    CollapsedElectricNode.fromSingleNode(end)
            )
        }

        fun fromString(line: String): Set<CollapsedElectricEdge> {
            val sourcePartName = line.substringBefore(": ")
            val destinationPartNames = line.substringAfter(": ").split(" ")
            return destinationPartNames.map { destinationPartName ->
                CollapsedElectricEdge.fromSingleEdge(setOf(sourcePartName, destinationPartName))
            }.toSet()
        }
    }

    fun contains(node: CollapsedElectricNode): Boolean {
        return start == node || end == node
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CollapsedElectricEdge
        return setOf(this.start, this.end) == setOf(other.start, other.end)
    }

    override fun hashCode(): Int {
        return setOf(start, end).hashCode()
    }

    override fun toString(): String {
        return "$start <-> $end"
    }

    /**
     * {{a}, {b}}.collapseWith({{a, f}}) = {{a, f}, {b}}
     * {{f}, {e}}.collapseWith({{a, f}}) = {{a, f}, {e}}
     */
    fun collapseWith(other: CollapsedElectricEdge): CollapsedElectricEdge {
        val theseNodes = this.start.collapseWith(this.end)
        // If nodes overlap in the start node, substitute the other start node with this entire node
        return if (theseNodes.nodes.intersect(other.start.nodes).isNotEmpty()) {
//            println("Substituting other start node with this entire node")
            CollapsedElectricEdge(
                    theseNodes,
                    other.end
            )
        } else if (theseNodes.nodes.intersect(other.end.nodes).isNotEmpty()) {
//            println("Substituting other end node with this entire node")
            CollapsedElectricEdge(
                    other.start,
                    theseNodes
            )
        } else {
            throw Exception("Trying to collapse edges with non-overlapping nodes: $this and $other")
        }
    }
}

data class ElectricGraph(val nodes: Set<String>, val edges: Set<CollapsedElectricEdge>) {

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
     *  - Pair (V_1, V_2) representing the minimum cut
     */
    fun findMinCut(): Triple<Int, Set<String>, Set<String>> {
        // Make a copy of the nodes and edges to avoid concurrent modification
        val collapsedNodes: MutableSet<CollapsedElectricNode> = nodes.map {
            CollapsedElectricNode.fromSingleNode(it)
        }.toMutableSet()
        val collapsedEdges: MutableSet<CollapsedElectricEdge> = edges.toList().toMutableSet()
        var contractedVerticesCount = nodes.size

        while (collapsedNodes.size > 2) {
            // Pick a random edge
            val currentEdge = collapsedEdges.random()
            collapsedEdges.remove(currentEdge)

            // Find all the nodes connected by this edge
            val edgesToCollapseStart = collapsedEdges.filter { it.contains(currentEdge.start) }.toSet()
            collapsedEdges.removeAll(edgesToCollapseStart)
            val edgesToCollapseEnd = collapsedEdges.filter { it.contains(currentEdge.end) }.toSet()
            collapsedEdges.removeAll(edgesToCollapseEnd)

            // For each of them, replace the start/end nodes with the collapsed node
            val collapsedEdgesStart = edgesToCollapseStart.map { edge ->
                currentEdge.collapseWith(edge)
            }
            val collapsedEdgesEnd = edgesToCollapseEnd.map { edge ->
                currentEdge.collapseWith(edge)
            }

            // Update edge set with the new collapsed edges
            collapsedEdges.addAll(collapsedEdgesStart)
            collapsedEdges.addAll(collapsedEdgesEnd)

            // Update node set with the new collapsed nodes
            collapsedNodes.remove(currentEdge.start)
            collapsedNodes.remove(currentEdge.end)
            collapsedNodes.add(currentEdge.start.collapseWith(currentEdge.end))
        }

        val (v1, v2) = collapsedNodes.toList()

        // Count the number of removed edges

        return Triple(collapsedEdges.size, v1.nodes, v2.nodes)
    }

    companion object {
        fun fromSimpleRepresentations(nodes: Set<String>, edges: Set<Set<String>>): ElectricGraph {
            val parsedEdges = edges.map { edge ->
                CollapsedElectricEdge.fromSingleEdge(edge)
                }.toSet()
            return ElectricGraph(nodes, parsedEdges)
        }
    }
}

class Day25 : PuzzleSolution {
    override val input: List<String> = readInput("Day25")
    override fun part1(input: List<String>): String {
        val edges = input.flatMap { line -> CollapsedElectricEdge.fromString(line) }.toSet()
        val nodes = edges.map { it.start.nodes + it.end.nodes }.flatten().toSet()
        val graph = ElectricGraph(nodes, edges)
        var (removedEdges, firstGroup, secondGroup) = graph.findMinCut()
        while (removedEdges != 3) {
            val collapsed = graph.findMinCut()
            removedEdges = collapsed.first
            firstGroup = collapsed.second
            secondGroup = collapsed.third

            println("Removed $removedEdges to divide the graph into \n$firstGroup\nand\n$secondGroup")
        }
        return (firstGroup.size * secondGroup.size).toString()
    }

    override fun part2(input: List<String>): String {
        return "Not implemented"
    }
}

fun main() {
    val solution = Day25()

    val testEdge1 = CollapsedElectricEdge.fromString("ntq: jqt hfx")
    val expectedEdge1 = setOf(
            CollapsedElectricEdge(
                    CollapsedElectricNode(setOf("ntq")),
                    CollapsedElectricNode(setOf("jqt"))
            ),
            CollapsedElectricEdge(
                    CollapsedElectricNode(setOf("ntq")),
                    CollapsedElectricNode(setOf("hfx"))
            )
    )
    check(testEdge1 == expectedEdge1) { "fromString didn't parse edge correctly; expected $expectedEdge1, got $testEdge1" }

    // {a, b}.collapseWith({a, f}) = {{a, f}, b}
    val testEdgeAB = CollapsedElectricEdge.fromSingleEdge(setOf("a", "b"))
    val testEdgeAF = CollapsedElectricEdge.fromSingleEdge(setOf("a", "f"))
    val collapsedEdgeABF = testEdgeAF.collapseWith(testEdgeAB)
    val expectedCollapsedABF = CollapsedElectricEdge(
            start = CollapsedElectricNode(setOf("a", "f")),
            end = CollapsedElectricNode(setOf("b"))
    )
    check(collapsedEdgeABF == expectedCollapsedABF) { "Collapse didn't work correctly; expected $expectedCollapsedABF, got $collapsedEdgeABF" }


    val testGraph = ElectricGraph.fromSimpleRepresentations(
            setOf("a", "b", "b", "e", "f", "g"),
            setOf(setOf("a", "b"), setOf("b", "f"), setOf("f", "e"), setOf("a", "e"), setOf("a", "f"), setOf("e", "g"),
                    setOf("b", "c"), setOf("c", "g"), setOf("f", "g"))
    )
    val x = testGraph.findMinCut()
//    println(x)

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day25_test")
    val testSolution1 = solution.part1(testInput)
//    check(testSolution1 == "54") { "Expected 54, got $testSolution1" }

//    val testSolution2 = solution.part2(testInput)
//    check(testSolution2 == "30") { "Expected 30, got $testSolution2" }

    solution.run()
}
