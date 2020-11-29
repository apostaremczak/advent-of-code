package apostaremczak.advent_of_code.day_06

import apostaremczak.advent_of_code.Puzzle
import apostaremczak.advent_of_code.day_06.OrbitMapUtils.OrbitMap

object UniversalOrbitMap extends Puzzle[String] {
  val day = 6

  implicit val orbitMap: OrbitMap = OrbitMapUtils.parseMapRecipes(input)

  /**
    * What is the total number of direct and indirect orbits in your map data?
    */
  def countOrbits(implicit orbitMap: OrbitMap): Int =
    orbitMap.values.map(_.orbitsCount).sum

  /**
    * What is the minimum number of orbital transfers required to move from the object
    * YOU are orbiting to the object SAN is orbiting?
    * (Between the objects they are orbiting - not between YOU and SAN.)
    */
  def findTransferToSanta(implicit orbitMap: OrbitMap): Int = {
    val myOrbit                = orbitMap(orbitMap("YOU").parentName.getOrElse("COM"))
    val santasOrbit            = orbitMap(orbitMap("SAN").parentName.getOrElse("COM"))
    val lastCommonPredecessors = myOrbit.lastCommonPredecessor(santasOrbit)
    val myDistToPred           = myOrbit.distanceFromCenter - lastCommonPredecessors.distanceFromCenter
    val santasDistToPred       = santasOrbit.distanceFromCenter - lastCommonPredecessors.distanceFromCenter

    myDistToPred + santasDistToPred
  }

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: $countOrbits")
    println(s"Solution to part 2: $findTransferToSanta")
  }
}
