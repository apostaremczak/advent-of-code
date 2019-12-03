package apostaremczak.advent_of_code.day_02

import apostaremczak.advent_of_code.Puzzle

import scala.annotation.{switch, tailrec}

object ProgramAlarm extends Puzzle[String] {
  type Memory = IndexedSeq[Int]

  val day         = 2
  val magicResult = 19690720

  def initialMemory(noun: Int, verb: Int): Memory =
    input.head
      .split(",")
      .map(augmentString(_).toInt)
      .toIndexedSeq
      .updated(1, noun)
      .updated(2, verb)

  /**
    * Substitute a value in memory by doing an operation from the first two positions
    * after an opcode and store the result in the first position.
    */
  def executeOperation(
      memory: Memory,
      codeAddress: Int,
      operation: (Int, Int) => Int
  ): Memory = {
    memory.updated(
      memory(codeAddress + 3),
      operation(
        memory(memory(codeAddress + 1)),
        memory(memory(codeAddress + 2))
      )
    )
  }

  /**
    *  What value is left at position 0 after the program halts?
    */
  @tailrec
  def readInstructions(memory: Memory, address: Int = 0): Int = {
    require(List(1, 2, 99).contains(memory(address)))

    val nextAddress = address + 4
    (memory(address): @switch) match {
      case 1 =>
        readInstructions(executeOperation(memory, address, _ + _), nextAddress)
      case 2 =>
        readInstructions(executeOperation(memory, address, _ * _), nextAddress)
      case 99 => memory.head
    }
  }

  /**
    * Find the input noun and verb that cause the program to produce
    * the output 19690720.
    * Each of the two input values will be between 0 and 99, inclusive.
    * What is 100 * noun + verb?
    */
  def findNounVerb: Int = {
    val inputRange = 0 to 99

    inputRange.flatMap { noun =>
      inputRange
        .find { verb =>
          readInstructions(initialMemory(noun, verb)) == magicResult
        }
        .map(100 * noun + _)
    }.head
  }

  def main(args: Array[String]): Unit = {
    //  Before running the program, replace position 1 with the value 12
    //  and replace position 2 with the value 2.
    println(s"Solution to part 1: ${readInstructions(initialMemory(12, 2))}")
    println(s"Solution to part 2: $findNounVerb")
  }
}
