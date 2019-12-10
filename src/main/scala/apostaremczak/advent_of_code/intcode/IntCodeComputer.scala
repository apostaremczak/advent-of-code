package apostaremczak.advent_of_code.intcode

import apostaremczak.advent_of_code.intcode.MemoryUtils.Memory

import scala.annotation.tailrec

class IntCodeComputer(memory: Memory) {
  @tailrec
  private def readInstruction(state: State): State = {
    state.opCode.operation match {
      case Halt      => state
      case operation => readInstruction(operation.calculate(state))
    }
  }

  def runProgram(computerInput: List[Int]): State =
    readInstruction(State(memory, input = computerInput))

  def runProgram(computerInput: Int): State =
    runProgram(List(computerInput))

  def diagnosticCode(computerInput: List[Int]): Int =
    runProgram(computerInput).outputs.last

  def diagnosticCode(computerInput: Int): Int =
    diagnosticCode(List(computerInput))
}
