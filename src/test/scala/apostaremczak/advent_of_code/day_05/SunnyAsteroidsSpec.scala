package apostaremczak.advent_of_code.day_05

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import apostaremczak.advent_of_code.day_05.MemoryUtils.Memory

class SunnyAsteroidsSpec extends AnyWordSpec with Matchers {
  "Memory" must {
    val memory = IndexedSeq(1002, 4, 3, 4, 33)

    "return value from position" when {
      "parameter mode is positional" in {
        memory.get(3, Positional) must equal(33)
        memory.get(1) must equal(33)
      }
      "parameter mode is immediate" in {
        memory.get(2, Immediate) must equal(3)
      }
    }
  }

  "SunnyAsteroids" must {
    "execute binary operation" when {
      "parameter mode is positional and immediate" in {
        val memory = IndexedSeq(1002, 4, 3, 4, 33)
        val posMode3 :: posMode2 :: posMode1 :: _ =
          f"${memory.instructions(0)}%05d".map(_.asDigit).toList

        val result = SunnyAsteroids
          .executeBinaryOperation(
            memory,
            0,
            _ * _,
            posMode1,
            posMode2
          )
          .instructions

        val expectedResult = IndexedSeq(1002, 4, 3, 4, 99)

        result must equal(expectedResult)
      }
    }
  }
}
