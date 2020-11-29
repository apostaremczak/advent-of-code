package apostaremczak.advent_of_code.intcode

import apostaremczak.advent_of_code.intcode.MemoryUtils.Memory

/**
  * Handles updates to the memory.
  */
case class State(
    memory: Memory,
    instructionPointer: Int = 0,
    input: List[Int] = Nil,
    outputs: List[Int] = Nil
) {
  lazy val opCode: OpCode = OpCode(memory.instructions(instructionPointer))
}
