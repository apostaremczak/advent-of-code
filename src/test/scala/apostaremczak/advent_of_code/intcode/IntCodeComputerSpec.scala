package apostaremczak.advent_of_code.intcode

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class IntCodeComputerSpec extends AnyWordSpec with Matchers {
  "IntCodeComputer" must {
    "calculate correct diagnostic code of a short program in position mode" in {
      val computer = new IntCodeComputer(
        IndexedSeq(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8)
      )
      computer.diagnosticCode(1) must equal(0)
      computer.diagnosticCode(100) must equal(0)
      computer.diagnosticCode(8) must equal(1)
    }

    "calculate correct diagnostic code of a short program in immediate mode" in {
      val computer = new IntCodeComputer(
        IndexedSeq(3, 3, 1108, -1, 8, 3, 4, 3, 99)
      )
      computer.diagnosticCode(1) must equal(0)
      computer.diagnosticCode(100) must equal(0)
      computer.diagnosticCode(8) must equal(1)
    }

    "calculate correct diagnostic code of a short program using jumps in position mode" in {
      val computer = new IntCodeComputer(
        IndexedSeq(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9)
      )
      computer.diagnosticCode(0) must equal(0)
      computer.diagnosticCode(100) must equal(1)
      computer.diagnosticCode(8) must equal(1)
    }

    "calculate correct diagnostic code of a short program using jumps in immediate mode" in {
      val computer = new IntCodeComputer(
        IndexedSeq(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1)
      )
      computer.diagnosticCode(0) must equal(0)
      computer.diagnosticCode(100) must equal(1)
      computer.diagnosticCode(8) must equal(1)
    }
  }
}
