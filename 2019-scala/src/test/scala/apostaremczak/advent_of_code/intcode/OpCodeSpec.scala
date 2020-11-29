package apostaremczak.advent_of_code.intcode

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class OpCodeSpec extends AnyWordSpec with Matchers {
  "Opcode" must {
    "parse opcodes with operations and parameter modes" in {
      // Code: 1
      OpCode(1) must equal(
        OpCode(Sum, List(Positional, Positional, Immediate))
      )
      OpCode(1101) must equal(
        OpCode(Sum, List(Immediate, Immediate, Immediate))
      )

      // Code: 2
      OpCode(1002) must equal(
        OpCode(Multiplication, List(Positional, Immediate, Immediate))
      )

      // Code: 3
      OpCode(3) must equal(
        OpCode(InputWriter, List(Immediate))
      )

      // Code: 4
      OpCode(4) must equal(
        OpCode(OutputUpdater, List(Positional))
      )

      // Code: 5
      OpCode(1105) must equal(
        OpCode(IfTrueJumper, List(Immediate, Immediate))
      )

      // Code: 6
      OpCode(1006) must equal(
        OpCode(IfFalseJumper, List(Positional, Immediate))
      )

      // Code: 7
      OpCode(7) must equal(
        OpCode(LessThanComparison, List(Positional, Positional, Immediate))
      )

      // Code: 8
      OpCode(108) must equal(
        OpCode(EqualityComparison, List(Immediate, Positional, Immediate))
      )

      // Code: 99
      OpCode(99) must equal(
        OpCode(Halt, Nil)
      )
    }
  }
}
