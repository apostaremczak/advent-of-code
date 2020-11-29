package apostaremczak.advent_of_code.day_12

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class NBodyProblemSpec extends AnyWordSpec with Matchers {
  "NBodyProblem" must {
    "calculate the total energy of a system after 10 iterations" in {
      val initialMoons = IndexedSeq(
        Moon(SpaceVector(-1, 0, 2)),
        Moon(SpaceVector(2, -10, -7)),
        Moon(SpaceVector(4, -8, 8)),
        Moon(SpaceVector(3, 5, -1))
      )

      NBodyProblem.totalSystemEnergy(initialMoons, 10) must equal(179)
    }

    "calculate the total energy of a system after 100 iterations" in {
      val initialMoons = IndexedSeq(
        Moon(SpaceVector(-8, -10, 0)),
        Moon(SpaceVector(5, 5, 10)),
        Moon(SpaceVector(2, -7, 3)),
        Moon(SpaceVector(9, -8, -3))
      )

      NBodyProblem.totalSystemEnergy(initialMoons, 100) must equal(1940)
    }
  }
}
