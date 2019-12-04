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

  def main(args: Array[String]): Unit = {
    println(s"Solution to part1: ${distanceBetweenCrossings(firstWire, secondWire)}")
  }
}
