package apostaremczak.advent_of_code.day_04

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SecureContainerSpec extends AnyWordSpec with Matchers {
  "Password" must {
    "verify that it is partially valid when more than two repetitions are allowed" in {
      Password("111111").isPartiallyValid(0, 121111) must be(true)
      Password("223450").isPartiallyValid(0, 243450) must be(false)
      Password("123789").isPartiallyValid(0, 133789) must be(false)
    }

    "verify that it is valid" in {
      Password("112233").isValid(0, 122233) must be(true)
      Password("123444").isValid(0, 124444) must be(false)
      // Even though 1 is repeated more than twice, it still contains a double 22
      Password("111122").isValid(0, 121122) must be(true)
      Password("111222").isValid(0, 121122) must be(false)
    }
  }
}
