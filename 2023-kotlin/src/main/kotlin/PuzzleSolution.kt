import java.math.BigInteger
import java.security.MessageDigest
import kotlin.system.measureTimeMillis

interface GenericPuzzleSolution<T : Any> {
    /**
     * Reads lines from the given input txt file and split it into lines.
     */
    fun readInput(name: String): List<String> =
            this.javaClass.getResource("$name.txt")?.readText()!!.lines().filter { it.isNotEmpty() }

    /**
     * Reads lines from the given input txt file without any splitting.
     */
    fun readInputNotSplit(name: String): String =
            this.javaClass.getResource("$name.txt")?.readText()!!

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

    fun part1(input: T): String

    fun part2(input: T): String

    val input: T

    fun part1(): String {
        return part1(input)
    }

    fun part2(): String {
        return part2(input)
    }

    fun run() {
        measureTimeMillis {
            println("Solution Part 1\n${part1(input)}")
        }.let { println("Part 1 took $it ms\n") }

        measureTimeMillis {
            println("Solution Part 2\n${part2(input)}")
        }.let { println("Part 2 took $it ms") }
    }

    /**
     * Calculate the greatest common denominator of two numbers
     */
    fun Long.gcd(other: Long): Long {
        return if(other == 0L) this else other.gcd(this % other)
    }

    /**
     * Calculate the least common multiple of a list of numbers
     */
    fun List<Long>.lcm(): Long {
        return this.reduce { acc, i -> acc * i / acc.gcd(i) }
    }
}

interface PuzzleSolution : GenericPuzzleSolution<List<String>>

interface PuzzleSolutionNonSplitInput : GenericPuzzleSolution<String>
