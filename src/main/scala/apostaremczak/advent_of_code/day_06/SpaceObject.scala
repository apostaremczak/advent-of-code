package apostaremczak.advent_of_code.day_06

import apostaremczak.advent_of_code.day_06.OrbitMapUtils.OrbitMap

case class SpaceObject(parentName: Option[String]) {

  /**
    * Distance (in direct space objects) from center of mass
    */
  def distanceFromCenter(implicit orbitMap: OrbitMap): Int = parentName match {
    // Object orbits nothing, thus it is the center of mass
    case None         => 0
    case Some(parent) => orbitMap(parent).distanceFromCenter + 1
  }

  def directOrbits: Int =
    if (parentName.isDefined) 1 else 0

  def indirectOrbits(implicit orbitMap: OrbitMap): Int =
    // Make sure center of mass doesn't give out negative value
    if (parentName.isDefined) distanceFromCenter - 1 else 0

  def orbitsCount(implicit orbitMap: OrbitMap): Int =
    directOrbits + indirectOrbits
}
