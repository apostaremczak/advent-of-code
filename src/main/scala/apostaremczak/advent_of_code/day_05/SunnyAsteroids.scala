package apostaremczak.advent_of_code.day_05

import apostaremczak.advent_of_code.CommaSeparatedPuzzle
import apostaremczak.advent_of_code.day_05.MemoryUtils.Memory

import scala.annotation.tailrec

object SunnyAsteroids extends CommaSeparatedPuzzle[Int] {
  val day = 5

  val memory: Memory = input.toIndexedSeq

  /**
    * Substitute a value in memory by doing an operation from the first two positions
    * after an opcode and store the result in the first position.
    */
  def executeBinaryOperation(
      memory: Memory,
      codeAddress: Int,
      operation: (Int, Int) => Int,
      firstArgMode: ParameterMode,
      secondArgMode: ParameterMode
  ): Memory = {
    val resultStorage = memory.instructions(codeAddress + 3)
    val firstArg      = memory.get(codeAddress + 1, firstArgMode)
    val secondArg     = memory.get(codeAddress + 2, secondArgMode)
    val opResult      = operation(firstArg, secondArg)

    memory.updated(
      memory.instructions(codeAddress + 3),
      operation(
        memory.get(codeAddress + 1, firstArgMode),
        memory.get(codeAddress + 2, secondArgMode)
      )
    )
  }

  @tailrec
  def readInstructions(
      memory: Memory,
      address: Int = 0,
      outputs: List[Int] = Nil
  )(implicit input: Int): List[Int] = {
    val opCode = f"${memory.instructions(address)}%05d".map(_.asDigit).toList

    opCode match {
      // Perform addition
      case _ :: posMode2 :: posMode1 :: _ :: 1 :: Nil =>
        readInstructions(
          executeBinaryOperation(memory, address, _ + _, posMode1, posMode2),
          address + 4,
          outputs
        )
      // Perform multiplication
      case _ :: posMode2 :: posMode1 :: _ :: 2 :: Nil =>
        readInstructions(
          executeBinaryOperation(memory, address, _ * _, posMode1, posMode2),
          address + 4,
          outputs
        )

      // Saves input
      case _ :: _ :: _ :: _ :: 3 :: Nil =>
        // Save the input at position given by the first argument
        readInstructions(
          memory.updated(memory.get(address + 1, Immediate), input),
          address + 2,
          outputs
        )
      // Outputs input
      case _ :: _ :: _ :: _ :: 4 :: Nil =>
        // Read the value from position given by the first argument and add it to outputs
        readInstructions(
          memory,
          address + 2,
          outputs ++ List(
            memory.get(memory.get(address + 1, Immediate), Immediate)
          )
        )

      // Jump if condition is non-zero
      case _ :: posMode2 :: posMode1 :: _ :: 5 :: Nil =>
        if (memory.get(address + 1, posMode1) != 0)
          readInstructions(
            memory,
            memory.get(address + 2, posMode2),
            outputs
          )
        else
          readInstructions(
            memory,
            address + 3,
            outputs
          )

      // Jump if condition is zero
      case _ :: posMode2 :: posMode1 :: _ :: 6 :: Nil =>
        if (memory.get(address + 1, posMode1) == 0)
          readInstructions(
            memory,
            memory.get(address + 2, posMode2),
            outputs
          )
        else
          readInstructions(
            memory,
            address + 3,
            outputs
          )

      // If the first parameter is less than the second parameter,
      // store 1 in the position given by the third parameter.
      // Otherwise, store 0.
      case posMode3 :: posMode2 :: posMode1 :: _ :: 7 :: Nil =>
        val first  = memory.get(address + 1, posMode1)
        val second = memory.get(address + 2, posMode2)
        val third  = memory.get(address + 3, Immediate)
        val newMemory =
          if (first < second)
            memory.updated(third, 1)
          else
            memory.updated(third, 0)

        readInstructions(
          newMemory,
          address + 4,
          outputs
        )

      // If the first parameter equals the second parameter,
      // store 1 in the position given by the third parameter.
      // Otherwise, store 0.
      case posMode3 :: posMode2 :: posMode1 :: _ :: 8 :: Nil =>
        val first  = memory.get(address + 1, posMode1)
        val second = memory.get(address + 2, posMode2)
        val third  = memory.get(address + 3, Immediate)
        val newMemory =
          if (first == second)
            memory.updated(third, 1)
          else
            memory.updated(third, 0)

        readInstructions(
          newMemory,
          address + 4,
          outputs
        )

      case _ :: _ :: _ :: 9 :: 9 :: Nil =>
        outputs
    }
  }

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: ${readInstructions(memory)(1)}")
    println(s"Solution to part 2: ${readInstructions(memory)(5)}")
  }
}
