package apostaremczak.advent_of_code.day_12

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SpaceVectorSpec extends AnyWordSpec with Matchers {
  "SpaceVector" must {
    "parse puzzle input" in {
      SpaceVector("<x=-8, y=-10, z=0>") must equal(SpaceVector(-8, -10, 0))
      SpaceVector("<x=5, y=5, z=10>") must equal(SpaceVector(5, 5, 10))
      SpaceVector("<x=2, y=-7, z=3>") must equal(SpaceVector(2, -7, 3))
      SpaceVector("<x=9, y=-8, z=-3>") must equal(SpaceVector(9, -8, -3))
    }
  }
}
