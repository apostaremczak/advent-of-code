data class PlanePoint(val x: Int, val y: Int)

data class Jenga(val bricks: MutableSet<Brick>, val heightMap: MutableMap<PlanePoint, Int>) {
    /**
     * Adds a new brick to the construction;
     * assumes the present construction is already stabilized
     */
    fun dropBrick(newBrick: Brick) {
        // Find the distance between the cube and the stable ground/structure underneath
        val toBeDroppedUnits = newBrick.cubes.minOf { cube ->
            cube.z - (heightMap[PlanePoint(cube.x, cube.y)] ?: 0) - 1
        }
        val droppedBrick = newBrick.dropBy(toBeDroppedUnits)
        addNewBrick(droppedBrick)
    }

    private fun addNewBrick(newBrick: Brick) {
        bricks.add(newBrick)
        val highestBrickPoint = newBrick.cubes.maxOf { it.z }
        newBrick.cubes.forEach { cube ->
            val currentHeight = heightMap[PlanePoint(cube.x, cube.y)] ?: 0
            heightMap[PlanePoint(cube.x, cube.y)] = maxOf(currentHeight, highestBrickPoint)
        }
    }

    companion object {
        fun empty(): Jenga {
            return Jenga(mutableSetOf(), mutableMapOf())
        }

        fun fromInput(input: List<String>): Jenga {
            // Sort the bricks in the order of bricks being closest to the ground
            val bricks = input.map { Brick.fromString(it) }.sortedBy { b -> b.cubes.minOf { c -> c.z } }
            val jenga = empty()
            bricks.forEach { jenga.dropBrick(it) }

            return jenga
        }
    }
}

data class Cube(val x: Int, val y: Int, val z: Int)

data class Brick(val cubes: Set<Cube>) {
    fun dropBy(units: Int): Brick {
        return Brick(cubes.map { it.copy(z = it.z - units) }.toSet())
    }

    /**
     * A brick is supporting another brick if it is directly below it
     */
    fun isSupporting(other: Brick): Boolean {
        val cubesBelowOther = other.cubes.map { it.copy(z = it.z - 1) }.toSet()
        return cubesBelowOther.intersect(this.cubes).isNotEmpty()
    }

    companion object {

        /**
         * 2,6,118~2,9,118
         * => Brick(setOf(Cube(2, 6, 118), Cube(2, 7, 118), Cube(2, 8, 118), Cube(2, 9, 118)))
         */
        fun fromString(representation: String): Brick {
            val (startPlanePoint, endPlanePoint) = representation.split("~").map { planePointTriple ->
                planePointTriple.split(",").map { it.toInt() }
            }
            val cubes: MutableSet<Cube> = mutableSetOf()
            (startPlanePoint[0]..endPlanePoint[0]).forEach { x ->
                (startPlanePoint[1]..endPlanePoint[1]).forEach { y ->
                    (startPlanePoint[2]..endPlanePoint[2]).forEach { z ->
                        cubes.add(Cube(x, y, z))
                    }
                }
            }

            return Brick(cubes)
        }
    }
}

class Day22 : PuzzleSolution {
    override val input: List<String> = readInput("Day22")

    /**
     * Once they've settled, consider disintegrating a single brick;
     * how many bricks could be safely chosen as the one to get disintegrated?
     */
    override fun part1(input: List<String>): String {
        // Sort the bricks in the order of bricks being closest to the ground
        val jenga = Jenga.fromInput(input)

        // Check the support of each brick - what bricks are supporting it?
        val bricksSupportedBy = jenga.bricks.associateWith { brick ->
            jenga.bricks.minus(setOf(brick)).filter { it.isSupporting(brick) }
        }

        // Which bricks can be disintegrated?
        val nonRemovableBricks = jenga.bricks.filter { brick ->
            // A brick cannot be removed if it is the only brick supporting another one
            val thisBrickSupports = bricksSupportedBy.filterValues { it.contains(brick) }
            thisBrickSupports.any { (_, v) -> v.size == 1 }
        }

        return (jenga.bricks.size - nonRemovableBricks.size).toString()
    }

    override fun part2(input: List<String>): String {
        val jenga = Jenga.fromInput(input)

        // Check the support of each brick - what bricks are supporting it?
        val bricksSupportedBy = jenga.bricks.associateWith { brick ->
            jenga.bricks.minus(setOf(brick)).filter { it.isSupporting(brick) }
        }

        val brickDependencies = jenga.bricks
                .associateWith { brick -> bricksSupportedBy.filterValues { it.contains(brick) }.keys }

        // Which bricks can be disintegrated? How many bricks would fall if they were removed?
        val nonRemovableBricks = jenga.bricks.filter { brick ->
            // A brick cannot be removed if it is the only brick supporting another one
            val thisBrickSupports = bricksSupportedBy.filterValues { it.contains(brick) }
            thisBrickSupports.any { (_, v) -> v.size == 1 }
        }

        // Considering those "non-removable" bricks, IF we remove them, how many bricks would fall?
        val fallenCounts = nonRemovableBricks.map { brickOfOrigin ->
            val fallenBricks = mutableSetOf(brickOfOrigin)
            val waitingToFall = ArrayDeque(brickDependencies[brickOfOrigin] ?: emptySet())

            while (waitingToFall.isNotEmpty()) {
                // A brick falls if all of its support has already fallen
                val currentlyInvestigated = waitingToFall.removeFirst()
                if (bricksSupportedBy[currentlyInvestigated]!!.all { fallenBricks.contains(it) }) {
                    fallenBricks.add(currentlyInvestigated)
                    waitingToFall.addAll(brickDependencies[currentlyInvestigated] ?: emptySet())
                }
            }

            // Do not count the fallen brick itself, only the chain reaction caused
            fallenBricks.size - 1
        }

        return fallenCounts.sum().toString()
    }
}

fun main() {
    val solution = Day22()

    // Test parsing bricks
    // 2,6,118~2,9,118 => Brick(setOf(Cube(2, 6, 118), Cube(2, 7, 118), Cube(2, 8, 118), Cube(2, 9, 118)))
    val testBrick1 = Brick.fromString("2,6,118~2,9,118")
    val expectedBrick1 = Brick(setOf(Cube(2, 6, 118), Cube(2, 7, 118), Cube(2, 8, 118), Cube(2, 9, 118)))
    check(testBrick1 == expectedBrick1) { "Expected $expectedBrick1, got $testBrick1" }

    val testBrick2 = Brick.fromString("2,2,2~2,2,2")
    val expectedBrick2 = Brick(setOf(Cube(2, 2, 2)))
    check(testBrick2 == expectedBrick2) { "Expected $expectedBrick2, got $testBrick2" }

    val testJenga1 = Jenga.empty()
    val testDroppedBrick1 = Brick(setOf(Cube(x = 1, y = 0, z = 1), Cube(x = 1, y = 1, z = 1), Cube(x = 1, y = 2, z = 1)))
    testJenga1.dropBrick(testDroppedBrick1)
    val expectedHeightMap1 = mutableMapOf(
            PlanePoint(1, 0) to 1,
            PlanePoint(1, 1) to 1,
            PlanePoint(1, 2) to 1
    )
    check(testJenga1.heightMap == expectedHeightMap1)
    val testDroppedBrick2 = Brick(setOf(Cube(x = 0, y = 0, z = 2), Cube(x = 1, y = 0, z = 2), Cube(x = 2, y = 0, z = 2)))
    testJenga1.dropBrick(testDroppedBrick2)
    val expectedHeightMap2 = mutableMapOf(
            PlanePoint(0, 0) to 2,
            PlanePoint(1, 0) to 2,
            PlanePoint(2, 0) to 2,
            PlanePoint(1, 1) to 1,
            PlanePoint(1, 2) to 1
    )
    check(testJenga1.heightMap == expectedHeightMap2) { "Expected $expectedHeightMap2, got $testJenga1.heightMap" }

    val testDroppedBrick3 = Brick(setOf(Cube(x = 0, y = 0, z = 5), Cube(x = 1, y = 0, z = 5), Cube(x = 2, y = 0, z = 5)))
    testJenga1.dropBrick(testDroppedBrick3)
    val expectedHeightMap3 = mutableMapOf(
            PlanePoint(0, 0) to 3,
            PlanePoint(1, 0) to 3,
            PlanePoint(2, 0) to 3,
            PlanePoint(1, 1) to 1,
            PlanePoint(1, 2) to 1
    )
    check(testJenga1.heightMap == expectedHeightMap3) { "Expected $expectedHeightMap3, got $testJenga1.heightMap" }

    val brickA = Brick(setOf(Cube(1, 0, 1), Cube(1, 1, 1), Cube(1, 2, 1)))
    val brickB = Brick(setOf(Cube(0, 0, 2), Cube(1, 0, 2), Cube(2, 0, 2)))
    check(brickA.isSupporting(brickB))

    val brickF = Brick(setOf(Cube(x=0, y=1, z=4), Cube(x=1, y=1, z=4), Cube(x=2, y=1, z=4)))
    val brickG = Brick(setOf(Cube(x=1, y=1, z=5), Cube(x=1, y=1, z=6)))
    check(brickF.isSupporting(brickG))

    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day22_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "5") { "Expected 5, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "7") { "Expected 7, got $testSolution2" }

    solution.run()
}
