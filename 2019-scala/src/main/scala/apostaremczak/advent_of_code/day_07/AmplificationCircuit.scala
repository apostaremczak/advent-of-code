package apostaremczak.advent_of_code.day_07

import apostaremczak.advent_of_code.CommaSeparatedPuzzle
import apostaremczak.advent_of_code.intcode.IntCodeComputer
import apostaremczak.advent_of_code.intcode.MemoryUtils.Memory

object AmplificationCircuit extends CommaSeparatedPuzzle[Int] {
  val day                   = 7
  val initialMemory: Memory = input.toIndexedSeq

  def chainAmplifiers(instructions: Memory, phases: IndexedSeq[Int]): Int =
    phases.foldLeft(0) { (inputSignal, phaseSetting) =>
      new IntCodeComputer(instructions)
        .diagnosticCode(List(phaseSetting, inputSignal))
    }

  def maxThrustSignal(instructions: Memory): Int =
    (0 to 4).permutations.map {
      chainAmplifiers(instructions, _)
    }.max

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: ${maxThrustSignal(initialMemory)}")
  }
}
