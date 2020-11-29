package apostaremczak.advent_of_code.day_01

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RocketEquationSpec extends AnyWordSpec with Matchers {
  "RocketEquation" should {
    "calculate module's fuel required" in {
      RocketEquation.moduleFuelRequired(12) must equal(2)
      RocketEquation.moduleFuelRequired(14) must equal(2)
      RocketEquation.moduleFuelRequired(1969) must equal(654)
      RocketEquation.moduleFuelRequired(100756) must equal(33583)
    }

    "calculate additional fuel for fuel added" in {
      RocketEquation.recursiveFuelRequired(14) must equal(2)
      RocketEquation.recursiveFuelRequired(1969) must equal(966)
      RocketEquation.recursiveFuelRequired(100756) must equal(50346)
    }
  }
}
