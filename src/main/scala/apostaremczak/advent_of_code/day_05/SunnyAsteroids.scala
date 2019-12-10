package apostaremczak.advent_of_code.day_05

import apostaremczak.advent_of_code.CommaSeparatedPuzzle
import apostaremczak.advent_of_code.intcode.IntCodeComputer
import apostaremczak.advent_of_code.intcode.MemoryUtils.Memory

object SunnyAsteroids extends CommaSeparatedPuzzle[Int] {
  val day = 5

  val memory: Memory = input.toIndexedSeq
  val computer: IntCodeComputer = new IntCodeComputer(memory)

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: ${computer.diagnosticCode(1)}")
    println(s"Solution to part 2: ${computer.diagnosticCode(5)}")
  }
}
