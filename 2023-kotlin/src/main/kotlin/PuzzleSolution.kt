import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.system.measureTimeMillis

interface PuzzleSolution {

    /**
     * Reads lines from the given input txt file.
     */
    fun readInput(name: String): List<String> =
            this.javaClass.getResource("$name.txt")?.readText()!!.lines().filter { it.isNotEmpty() }

    /**
     * Converts string to md5 hash.
     */
    fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
            .toString(16)
            .padStart(32, '0')

    /**
     * The cleaner shorthand for printing output.
     */
    fun Any?.println() = println(this)

    fun part1(input: List<String>): Int

    fun part2(input: List<String>): Int

    val input: List<String>

    fun run() {
        measureTimeMillis {
            println("Solution Part 1\n${part1(input)}")
        }.let { println("Part 1 took $it ms\n") }

        measureTimeMillis {
            println("Solution Part 2\n${part2(input)}")
        }.let { println("Part 2 took $it ms") }
    }
}
