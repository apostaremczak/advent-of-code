package apostaremczak.advent_of_code.day_03

import apostaremczak.advent_of_code.Puzzle

object CrossedWires extends Puzzle[String] {
  val day = 3

  val List(firstWire, secondWire) = input.map { instructions =>
    Wire(instructions.split(",").toList)
  }

  /**
    * What is the Manhattan distance from the central port to the closest intersection?
    */
  def distanceBetweenCrossings(wire1: Wire, wire2: Wire): Int =
    wire1.intersect(wire2).map(_.distance(Port.central)).min

  /**
    * Calculate the number of steps each wire takes to reach each intersection;
    * choose the intersection where the sum of both wires' steps is lowest.
    * What is the fewest combined steps the wires must take to reach an intersection?
    */
  def smallestDistanceToCrossing(wire1: Wire, wire2: Wire): Int =
    wire1
      .intersect(wire2)
      .map { crossingPort: Port =>
        wire1.numStepsToPort(crossingPort) + wire2.numStepsToPort(crossingPort)
      }
      .min

  def main(args: Array[String]): Unit = {
    println(
      s"Solution to part1: ${distanceBetweenCrossings(firstWire, secondWire)}"
    )

    println(
      s"Solution to part2: ${smallestDistanceToCrossing(firstWire, secondWire)}"
    )
  }
}
