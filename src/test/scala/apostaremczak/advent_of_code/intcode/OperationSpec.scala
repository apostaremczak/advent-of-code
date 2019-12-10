package apostaremczak.advent_of_code.intcode

import apostaremczak.advent_of_code.intcode.MemoryUtils._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class OperationSpec extends AnyWordSpec with Matchers {
  "Operations" must {
    "correctly update memory based on extracted parameters and their modes" when {
      "executing Sum" in {
        val state       = State(IndexedSeq(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50))
        val resultState = Sum.calculate(state)
        val expectedMemory =
          IndexedSeq(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)

        resultState.memory.instructions must equal(expectedMemory)
        resultState.instructionPointer must equal(4)
      }

      "executing Multiplication" in {
        val state          = State(IndexedSeq(1002, 4, 3, 4, 33))
        val expectedMemory = IndexedSeq(1002, 4, 3, 4, 99)
        val resultState    = Multiplication.calculate(state)

        resultState.memory.instructions must equal(expectedMemory)
        resultState.instructionPointer must equal(4)
      }

      "executing InputWriter" in {
        val state = State(IndexedSeq(3, 0), input = List(1))

        InputWriter.calculate(state).memory.instructions must equal(
          IndexedSeq(1, 0)
        )
      }

      "executing OutputUpdater" in {
        val state       = State(IndexedSeq(4, 5, 0, 3, 2, 3))
        val resultState = OutputUpdater.calculate(state)
        val expectedState = state.copy(
          instructionPointer = state.instructionPointer + 2,
          outputs = List(3)
        )

        resultState must equal(expectedState)
      }

      "executing IfTrueJumper" in {
        // Compared argument is non-zero, make a jump
        IfTrueJumper
          .calculate(State(IndexedSeq(1105, 12, 4, 5)))
          .instructionPointer must equal(4)
        // Compared argument is zero, don't jump
        IfTrueJumper
          .calculate(State(IndexedSeq(1105, 0, 4, 5)))
          .instructionPointer must equal(3)
      }

      "executing IfFalseJumper" in {
        // Compared argument is zero, make a jump
        IfFalseJumper
          .calculate(State(IndexedSeq(1105, 0, 4, 5)))
          .instructionPointer must equal(4)
        // Compared argument is not zero, don't jump
        IfFalseJumper
          .calculate(State(IndexedSeq(1105, 12, 4, 5)))
          .instructionPointer must equal(3)
      }
    }
  }
}
