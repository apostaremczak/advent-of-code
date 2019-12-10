package apostaremczak.advent_of_code.intcode

import apostaremczak.advent_of_code.intcode.MemoryUtils.Memory

import scala.annotation.tailrec

class IntCodeComputer(memory: Memory) {
  @tailrec
  private def readInstruction(state: State): List[Int] = {
    state.opCode.operation match {
      case Halt      => state.outputs
      case operation => readInstruction(operation.calculate(state))
    }
  }

  def allOutputs(computerInput: Int): List[Int] = {
    val initialState = State(memory, input = computerInput)
    readInstruction(initialState)
  }

  def diagnosticCode(computerInput: Int): Int = allOutputs(computerInput).last
}
