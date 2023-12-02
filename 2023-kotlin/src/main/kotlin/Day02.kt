data class GameSet(val green: Int = 0, val red: Int = 0, val blue: Int = 0) {
    companion object {
        fun fromString(input: String): GameSet {
            val colors = listOf("green", "red", "blue")
            val colorCounts: Map<String, Int> = colors
                    .associateWith { color ->
                        val countMatch = Regex("(?<count>\\d+) $color").find(input)?.groups?.get("count")
                        countMatch?.value?.toInt() ?: 0
                    }
                    .withDefault { 0 }

            return GameSet(
                    colorCounts["green"] ?: 0,
                    colorCounts["red"] ?: 0,
                    colorCounts["blue"] ?: 0
            )
        }
    }
}

data class Game(val id: Int, val sets: List<GameSet>) {
    companion object {
        fun fromString(input: String): Game {
            val gameIdMatches = Regex("Game (?<gameId>\\d+).+").matchEntire(input)
            val gameId = gameIdMatches?.groups?.get("gameId")?.value?.toInt() ?: -1
            val sets = input.replace(Regex("Game \\d+: "), "").split("; ")
            val gameSets = sets.map { GameSet.fromString(it) }

            return Game(gameId, gameSets)
        }
    }

    // Determines is a game would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes
    fun isPossible(maxRed: Int = 12, maxGreen: Int = 13, maxBlue: Int = 14): Boolean = sets.all { set ->
        set.red <= maxRed && set.green <= maxGreen && set.blue <= maxBlue
    }

    fun getPower(): Int {
        val maxGreen = sets.maxOf { it.green }
        val maxRed = sets.maxOf { it.red }
        val maxBlue = sets.maxOf { it.blue }

        return maxGreen * maxRed * maxBlue
    }
}

class Day02 : PuzzleSolution {
    override val input: List<String> = readInput("Day02")

    override fun part1(input: List<String>): Int {
        val games = input.map { Game.fromString(it) }
        val possibleGames = games.filter { it.isPossible() }.map { it.id }
        return possibleGames.sum()
    }

    override fun part2(input: List<String>): Int {
        val gamePowers = input.map { Game.fromString(it) }.map { it.getPower() }
        return gamePowers.sum()
    }
}

fun main() {
    val solution = Day02()

    // test if implementation meets criteria from the description, like:
    val validTestGame = Game.fromString("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")
    check(validTestGame.isPossible())

    val invalidTestGameStr = "Game 100: 1 red, 14 green; 17 green, 12 red; 3 green, 7 red, 3 blue; 4 green, 13 red, 3 blue; 5 green, 11 red, 5 blue"
    val invalidTestGame = Game.fromString(invalidTestGameStr)
    check(invalidTestGame.id == 100)
    check(!invalidTestGame.isPossible())

    val testInput = readInput("Day02_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == 8) { "Expected 8, got $testSolution1" }

    check(validTestGame.getPower() == 48)
    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == 2286) { "Expected 2286, got $testSolution2" }

    solution.run()
}
