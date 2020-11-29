package apostaremczak.advent_of_code.intcode

import apostaremczak.advent_of_code.intcode.MemoryUtils.Memory

import scala.annotation.tailrec

class IntCodeComputer(memory: Memory) {
  @tailrec
  private def readInstruction(state: State): State = {
    state.opCode.operation match {
      case Halt        => state
      case InputWriter =>
        // If there's no input currently buffered and to be taken,
        // stop the machine and potentially wait for new input
        state.input match {
          case Nil => state
          case _   => readInstruction(InputWriter.calculate(state))
        }
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
