package apostaremczak.advent_of_code.intcode

import apostaremczak.advent_of_code.intcode.MemoryUtils.Memory

import scala.annotation.tailrec

class IntCodeComputer(memory: Memory) {
  @tailrec
  private def readInstruction(state: State): (Memory, List[Int]) = {
    state.opCode.operation match {
      case Halt      => (state.memory, state.outputs)
      case operation => readInstruction(operation.calculate(state))
    }
  }

  def runProgram(computerInput: Int): (Memory, List[Int]) =
    readInstruction(State(memory, input = List(computerInput)))

  def runProgram(computerInput: List[Int]): (Memory, List[Int]) =
    readInstruction(State(memory, input = computerInput))

  def diagnosticCode(computerInput: Int): Int =
    runProgram(computerInput)._2.last

  def diagnosticCode(computerInput: List[Int]): Int =
    runProgram(computerInput)._2.last
}
