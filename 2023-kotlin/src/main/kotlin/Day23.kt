import java.util.*

enum class ForestTileType {
    PATH {
        override val possibleDirections: Set<Pair<Int, Int>> = setOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
    },
    FOREST {
        override val possibleDirections: Set<Pair<Int, Int>> = emptySet()
    },
    SLOPE_NORTH {
        override val possibleDirections: Set<Pair<Int, Int>> = setOf(Pair(-1, 0))
    },
    SLOPE_SOUTH {
        override val possibleDirections: Set<Pair<Int, Int>> = setOf(Pair(1, 0))
    },
    SLOPE_WEST {
        override val possibleDirections: Set<Pair<Int, Int>> = setOf(Pair(0, -1))
    },
    SLOPE_EAST {
        override val possibleDirections: Set<Pair<Int, Int>> = setOf(Pair(0, 1))
    };

    abstract val possibleDirections: Set<Pair<Int, Int>>
}

class ForestTileNode(
        val positionRow: Int,
        val positionCol: Int,
        val tileType: ForestTileType,
        val reachableNodes: MutableSet<ForestTileNode>
) {
    override fun hashCode(): Int {
        return Pair(positionRow, positionCol).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ForestTileNode

        if (positionRow != other.positionRow) return false
        if (positionCol != other.positionCol) return false
        if (tileType != other.tileType) return false

        return true
    }
}

data class ForestGraph(val tiles: Map<Pair<Int, Int>, ForestTileNode>) {

    /**
     * Use BFS to find all the paths between the start and end nodes
     */
    fun findPaths(start: ForestTileNode, end: ForestTileNode): List<List<ForestTileNode>> {
        val queue = LinkedList<List<ForestTileNode>>()
        val paths = mutableListOf<List<ForestTileNode>>()

        queue.add(listOf(start))
        while (queue.isNotEmpty()) {
            val currentPath = queue.removeFirst()
            val currentNode = currentPath.last()

            // Get neighbors of current node
            val neighbors = currentNode.reachableNodes
            // Check if neighbor is the end node
            if (neighbors.any { it == end }) {
                val pathCopy = currentPath.toMutableList()
                pathCopy.add(end)
                paths.add(pathCopy)
            }
            // Continue searching from neighbors if they haven't been visited yet
            queue.addAll(currentNode.reachableNodes.filter { neighbor ->
                neighbor !in currentPath
            }.map { neighbor -> currentPath + neighbor })
        }

        return paths
    }

    /**
     * Use DFS to find the longest path between the start and end nodes
     */
//    fun findLongestPath(start: ForestTileNode, end: ForestTileNode): List<ForestTileNode> {
//
//    }
}

fun List<List<ForestTileType>>.toGraph(): ForestGraph {
    // Turn each Path and Slope element into a node
    // Two nodes are connected if they are adjacent, and we can traverse between them
    val nodes: Map<Pair<Int, Int>, ForestTileNode> = this.flatMapIndexed { row, tiles ->
        tiles.mapIndexedNotNull { col, tileType ->
            when (tileType) {
                ForestTileType.FOREST -> null
                else -> ForestTileNode(row, col, tileType, mutableSetOf())
            }
        }
    }.associateBy { Pair(it.positionRow, it.positionCol) }

    // Connect nodes if they are adjacent
    val parsedNodes = nodes.mapValues { (position, node) ->
        node.reachableNodes.addAll(
                node.tileType.possibleDirections.mapNotNull { direction ->
                    val nextPosition = Pair(
                            position.first + direction.first,
                            position.second + direction.second
                    )
                    nodes[nextPosition]
                }
        )
        node
    }

    return ForestGraph(parsedNodes)
}


class Day23 : PuzzleSolution {
    override val input: List<String> = readInput("Day23")
    override fun part1(input: List<String>): String {
        val forestParsed = input.map { line ->
            line.map { tileChar ->
                when (tileChar) {
                    '#' -> ForestTileType.FOREST
                    '.' -> ForestTileType.PATH
                    '^' -> ForestTileType.SLOPE_NORTH
                    '>' -> ForestTileType.SLOPE_EAST
                    'v' -> ForestTileType.SLOPE_SOUTH
                    '<' -> ForestTileType.SLOPE_WEST
                    else -> throw IllegalArgumentException("Invalid tile character: $tileChar")
                }
            }
        }

        val forestGraph = forestParsed.toGraph()

        val startNode = forestGraph.tiles.filterValues { it.positionRow == 0 && it.tileType == ForestTileType.PATH }.values.first()
        val endNode = forestGraph.tiles.filterValues { it.positionRow == forestParsed.lastIndex && it.tileType == ForestTileType.PATH }.values.first()
        val pathLengths = forestGraph.findPaths(startNode, endNode).map { it.size - 1 }

        return pathLengths.max().toString()
    }

    override fun part2(input: List<String>): String {
        val forestParsed = input.map { line ->
            line.map { tileChar ->
                when (tileChar) {
                    '#' -> ForestTileType.FOREST
                    else -> ForestTileType.PATH
                }
            }
        }

        val forestGraph = forestParsed.toGraph()

        val startNode = forestGraph.tiles.filterValues { it.positionRow == 0 && it.tileType == ForestTileType.PATH }.values.first()
        val endNode = forestGraph.tiles.filterValues { it.positionRow == forestParsed.lastIndex && it.tileType == ForestTileType.PATH }.values.first()
        val pathLengths = forestGraph.findPaths(startNode, endNode).map { it.size - 1 }

        return pathLengths.max().toString()
    }
}

fun main() {
    val solution = Day23()

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day23_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "94") { "Expected 94, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "154") { "Expected 154, got $testSolution2" }

    solution.run()
}
