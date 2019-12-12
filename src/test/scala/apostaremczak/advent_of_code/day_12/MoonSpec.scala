package apostaremczak.advent_of_code.day_12

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MoonSpec extends AnyWordSpec with Matchers {
  "Moon" must {
    "apply gravity of one moon to another" in {
      val Ganymede = Moon(SpaceVector(3, 0, 0))
      val Callisto = Moon(SpaceVector(5, 0, 0))

      Ganymede.applyGravityEffect(Callisto).x must equal(1)
    }

    "apply gravity to a system of moons" in {

      /**
        * <x=-1, y=0, z=2>
        * <x=2, y=-10, z=-7>
        * <x=4, y=-8, z=8>
        * <x=3, y=5, z=-1>
        */
      val moons = IndexedSeq(
        Moon(SpaceVector(-1, 0, 2)),
        Moon(SpaceVector(2, -10, -7)),
        Moon(SpaceVector(4, -8, 8)),
        Moon(SpaceVector(3, 5, -1))
      )

      /**
        * After 1 step:
        * pos=<x= 2, y=-1, z= 1>, vel=<x= 3, y=-1, z=-1>
        * pos=<x= 3, y=-7, z=-4>, vel=<x= 1, y= 3, z= 3>
        * pos=<x= 1, y=-7, z= 5>, vel=<x=-3, y= 1, z=-3>
        * pos=<x= 2, y= 2, z= 0>, vel=<x=-1, y=-3, z= 1>
        */
      val expectedSystem = IndexedSeq(
        Moon(SpaceVector(2, -1, 1), SpaceVector(3, -1, -1)),
        Moon(SpaceVector(3, -7, -4), SpaceVector(1, 3, 3)),
        Moon(SpaceVector(1, -7, 5), SpaceVector(-3, 1, -3)),
        Moon(SpaceVector(2, 2, 0), SpaceVector(-1, -3, 1))
      )

      Moon.applyGravityToSystem(moons) must equal(expectedSystem)
    }

    "calculate total energy of one moon" in {
      val moon = Moon(SpaceVector(2, 1, -3), SpaceVector(-3, -2, 1))

      moon.totalEnergy must equal(36)
    }

    "calculate total energy of a system" in {

      /**
        * pos=<x= 2, y= 1, z=-3>, vel=<x=-3, y=-2, z= 1>
        * pos=<x= 1, y=-8, z= 0>, vel=<x=-1, y= 1, z= 3>
        * pos=<x= 3, y=-6, z= 1>, vel=<x= 3, y= 2, z=-3>
        * pos=<x= 2, y= 0, z= 4>, vel=<x= 1, y=-1, z=-1>
        */
      val system = IndexedSeq(
        Moon(SpaceVector(2, 1, -3), SpaceVector(-3, -2, 1)),
        Moon(SpaceVector(1, -8, 0), SpaceVector(-1, 1, 3)),
        Moon(SpaceVector(3, -6, 1), SpaceVector(3, 2, -3)),
        Moon(SpaceVector(2, 0, 4), SpaceVector(1, -1, -1))
      )

      Moon.systemEnergy(system) must equal(179)
    }
  }
}
