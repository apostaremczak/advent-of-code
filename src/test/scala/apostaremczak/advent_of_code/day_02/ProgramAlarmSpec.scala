package apostaremczak.advent_of_code.day_02

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ProgramAlarmSpec extends AnyWordSpec with Matchers {
  "ProgramAlarm" should {
    "execute addition on Memory" in {
      val memory   = IndexedSeq(1, 0, 0, 0, 99)
      val expected = IndexedSeq(2, 0, 0, 0, 99)
      val result   = ProgramAlarm.executeOperation(memory, 0, _ + _)

      result must equal(expected)
    }

    "execute multiplication on Memory" in {
      val memory   = IndexedSeq(2, 3, 0, 3, 99)
      val expected = IndexedSeq(2, 3, 0, 6, 99)
      val result   = ProgramAlarm.executeOperation(memory, 0, _ * _)

      result must equal(expected)
    }

    "read instructions and find the new first value" in {
      val memory   = IndexedSeq(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)
      val expected = 3500
      val result   = ProgramAlarm.readInstructions(memory)

      result must equal(expected)
    }
  }
}
