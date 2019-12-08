package apostaremczak.advent_of_code.day_06

import apostaremczak.advent_of_code.Puzzle
import apostaremczak.advent_of_code.day_06.OrbitMapUtils.OrbitMap

object UniversalOrbitMap extends Puzzle[String] {
  val day = 6

  implicit val orbitMap: OrbitMap = OrbitMapUtils.parseMapRecipes(input)

  def countOrbits(implicit orbitMap: OrbitMap): Int =
    orbitMap.values.map(_.orbitsCount).sum

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: $countOrbits")
  }
}
