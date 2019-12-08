package apostaremczak.advent_of_code.day_06

import apostaremczak.advent_of_code.day_06.OrbitMapUtils.OrbitMap
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UniversalOrbitMapSpec extends AnyWordSpec with Matchers {
  "OrbitMapUtils" must {
    "parse orbit instructions" in {
      val instructions = List(
        "COM)B",
        "B)C",
        "C)D",
        "D)E"
      )

      val expectedOrbitMap = Map(
        "COM" -> SpaceObject(None),
        "B" -> SpaceObject(Some("COM")),
        "C" -> SpaceObject(Some("B")),
        "D" -> SpaceObject(Some("C")),
        "E" -> SpaceObject(Some("D"))
      )

      OrbitMapUtils.parseMapRecipes(instructions) must equal(expectedOrbitMap)
    }
  }

  "SpaceObject" must {
    implicit val orbitMap: OrbitMap = Map(
      "COM" -> SpaceObject(None),
      "B" -> SpaceObject(Some("COM")),
      "C" -> SpaceObject(Some("B")),
      "D" -> SpaceObject(Some("C")),
      "E" -> SpaceObject(Some("D")),
      "F" -> SpaceObject(Some("E")),
      "G" -> SpaceObject(Some("B")),
      "H" -> SpaceObject(Some("G"))
    )

    "count direct orbits" in {
      orbitMap("COM").directOrbits must be(0)
      orbitMap("B").directOrbits must be(1)
    }

    "count indirect orbits" in {
      orbitMap("COM").indirectOrbits must be(0)
      orbitMap("C").indirectOrbits must be(1)
      orbitMap("B").indirectOrbits must be(0)
      orbitMap("E").indirectOrbits must be(3)
      orbitMap("H").indirectOrbits must be(2)
    }
  }

  "UniversalOrbitMap" must {
    "count direct and indirect orbits in a system" in {
      implicit val orbitMap: OrbitMap = Map(
        "COM" -> SpaceObject(None),
        "B" -> SpaceObject(Some("COM")),
        "C" -> SpaceObject(Some("B")),
        "D" -> SpaceObject(Some("C")),
        "E" -> SpaceObject(Some("D")),
        "F" -> SpaceObject(Some("E")),
        "G" -> SpaceObject(Some("B")),
        "H" -> SpaceObject(Some("G")),
        "I" -> SpaceObject(Some("D")),
        "J" -> SpaceObject(Some("E")),
        "K" -> SpaceObject(Some("J")),
        "L" -> SpaceObject(Some("K"))
      )

      UniversalOrbitMap.countOrbits must be(42)
    }
  }
}
