package apostaremczak.advent_of_code.day_07

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AmplificationCircuitSpec extends AnyWordSpec with Matchers {
  "AmplificationCircuit" must {
    "find maximal thrust signal" in {
      def findMax(program: Int*): Int =
        AmplificationCircuit.maxThrustSignal(program.toIndexedSeq)

      findMax(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0,
        0) must equal(43210)

      findMax(3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23,
        1, 24, 23, 23, 4, 23, 99, 0, 0) must equal(54321)

      findMax(3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33,
        1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0,
        0) must equal(65210)
    }
  }
}
