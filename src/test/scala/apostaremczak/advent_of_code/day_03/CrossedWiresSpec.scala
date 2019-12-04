package apostaremczak.advent_of_code.day_03

import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CrossedWiresSpec
    extends AnyWordSpec
    with Matchers
    with PrivateMethodTester {

  "PathComponent" must {
    "be created from written instructions" in {
      PathComponent.apply("U87") must equal(PathComponent(Up, 87))
      PathComponent.apply("D63") must equal(PathComponent(Down, 63))
      PathComponent.apply("L72") must equal(PathComponent(Left, 72))
      PathComponent.apply("R8") must equal(PathComponent(Right, 8))
    }
  }

  "Port" must {
    "calculate Manhattan distance to another port" in {
      Port(0, 0).distance(Port(3, 3)) must equal(6)
    }
  }

  "Wire" must {

    "extend path by a path component when going up" in {
      val resultPath =
        Wire.addPathComponent(IndexedSeq(Port.central), PathComponent(Up, 3))
      val expectedPath = Set(Port(0, 0), Port(0, 1), Port(0, 2), Port(0, 3))

      resultPath.toSet must equal(expectedPath)
    }

    "extend path by a path component when going left" in {
      val resultPath =
        Wire.addPathComponent(IndexedSeq(Port(3, 1)), PathComponent(Left, 1))
      val expectedPath = Set(Port(3, 1), Port(2, 1))

      resultPath.toSet must equal(expectedPath)
    }

    "cross ports specified by instructions" in {
      val instructions = List("R3", "U2", "L1")
      val createdWire  = Wire(instructions)
      val expectedWire = Wire(
        IndexedSeq(
          Port(0, 0),
          Port(1, 0),
          Port(2, 0),
          Port(3, 0),
          Port(3, 1),
          Port(3, 2),
          Port(2, 2)
        )
      )

      createdWire.path.toSet must equal(expectedWire.path.toSet)
    }

    "find crossing point with another wire" in {
      val wire1        = Wire(List("R3", "U2", "L1"))
      val wire2        = Wire(List("U1", "R4"))
      val intersection = Set(Port(3, 1))

      wire1.intersect(wire2) must equal(intersection)
    }
  }

  "CrossedWires" must {
    "find minimal distance between two crossed wired and the central port" in {
      val wire1 = Wire(List("R8", "U5", "L5", "D3"))
      val wire2 = Wire(List("U7", "R6", "D4", "L4"))

      CrossedWires.distanceBetweenCrossings(wire1, wire2) must equal(6)
    }
  }
}
