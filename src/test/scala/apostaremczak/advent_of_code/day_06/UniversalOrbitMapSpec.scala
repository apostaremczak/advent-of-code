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
        "COM" -> SpaceObject("COM", None),
        "B"   -> SpaceObject("B", Some("COM")),
        "C"   -> SpaceObject("C", Some("B")),
        "D"   -> SpaceObject("D", Some("C")),
        "E"   -> SpaceObject("E", Some("D"))
      )

      OrbitMapUtils.parseMapRecipes(instructions) must equal(expectedOrbitMap)
    }
  }

  "SpaceObject" must {
    implicit val orbitMap: OrbitMap = Map(
      "COM" -> SpaceObject("COM", None),
      "B"   -> SpaceObject("B", Some("COM")),
      "C"   -> SpaceObject("C", Some("B")),
      "D"   -> SpaceObject("D", Some("C")),
      "E"   -> SpaceObject("E", Some("D")),
      "F"   -> SpaceObject("F", Some("E")),
      "G"   -> SpaceObject("G", Some("B")),
      "H"   -> SpaceObject("H", Some("G"))
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

    "find object's predecessors" in {
      orbitMap("H").predecessors must equal(
        List(
          orbitMap("G"),
          orbitMap("B"),
          orbitMap("COM")
        )
      )

      orbitMap("D").predecessors must equal(
        List(
          orbitMap("C"),
          orbitMap("B"),
          orbitMap("COM")
        )
      )

      orbitMap("COM").predecessors must be(Nil)
    }

    "find last common predecessor of two objects" in {
      val H = orbitMap("H")
      val D = orbitMap("D")
      val B = orbitMap("B")

      H.lastCommonPredecessor(D) must equal(B)
    }
  }

  "UniversalOrbitMap" must {
    "count direct and indirect orbits in a system" in {
      implicit val orbitMap: OrbitMap = Map(
        "COM" -> SpaceObject("COM", None),
        "B"   -> SpaceObject("B", Some("COM")),
        "C"   -> SpaceObject("C", Some("B")),
        "D"   -> SpaceObject("D", Some("C")),
        "E"   -> SpaceObject("E", Some("D")),
        "F"   -> SpaceObject("F", Some("E")),
        "G"   -> SpaceObject("G", Some("B")),
        "H"   -> SpaceObject("H", Some("G")),
        "I"   -> SpaceObject("I", Some("D")),
        "J"   -> SpaceObject("J", Some("E")),
        "K"   -> SpaceObject("K", Some("J")),
        "L"   -> SpaceObject("L", Some("K"))
      )

      UniversalOrbitMap.countOrbits must be(42)
    }

    "count the number of transfers between me and santa" in {
      implicit val orbitMap: OrbitMap = Map(
        "COM" -> SpaceObject("COM", None),
        "B"   -> SpaceObject("B", Some("COM")),
        "C"   -> SpaceObject("C", Some("B")),
        "D"   -> SpaceObject("D", Some("C")),
        "E"   -> SpaceObject("E", Some("D")),
        "F"   -> SpaceObject("F", Some("E")),
        "G"   -> SpaceObject("G", Some("B")),
        "H"   -> SpaceObject("H", Some("G")),
        "I"   -> SpaceObject("I", Some("D")),
        "J"   -> SpaceObject("J", Some("E")),
        "K"   -> SpaceObject("K", Some("J")),
        "L"   -> SpaceObject("L", Some("K")),
        "YOU" -> SpaceObject("YOU", Some("K")),
        "SAN" -> SpaceObject("SAN", Some("I"))
      )

      UniversalOrbitMap.findTransferToSanta must equal(4)
    }
  }
}
