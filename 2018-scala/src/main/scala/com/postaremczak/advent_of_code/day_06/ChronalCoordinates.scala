package com.postaremczak.advent_of_code.day_06

import com.postaremczak.advent_of_code.Solution

object ChronalCoordinates extends Solution(adventDay = 6) {

  case class GridPoint(
                        self: Point,
                        closest: Point
                      )

  val pointsOfInterest: Seq[Point] = puzzleInput.read.map(Point(_))

  // Find the smallest rectangle enclosing the points
  val locations: Grid = new Grid(pointsOfInterest)

  // Calculate distances from the location to all the points
  val distances: Map[Point, Map[Point, Int]] = locations
    .points
    .map { p: Point => (p, p.getDistances(pointsOfInterest)) }
    .toMap

  def findLargestArea: Int = {
    // Map the closest point of interest to each point on the grid
    val closestPoints = locations
      .points
      .flatMap {
        gridPoint: Point =>
          for {
            closest <- gridPoint.findClosest(distances(gridPoint))
          } yield GridPoint(gridPoint, closest)
      }

    // Drop the points which areas touch the grid edge - they lead to infinite results
    val onEdges = closestPoints
      .filter {
        gridPoint: GridPoint => gridPoint.self.isOnGridEdge(locations)
      }
      .map(_.closest)
      .toSet

    pointsOfInterest
      .filter(!onEdges.contains(_))
      .map { p: Point => closestPoints.count(_.closest == p) }
      .max
  }

  def findSafeAreaSize(maxTotalDistance: Int): Int = {
    distances
      .map {
        case (_, distances: Map[Point, Int]) =>
          distances
          .values
          .sum
      }
      .count(_ < maxTotalDistance)
  }

  def main(args: Array[String]): Unit = {
    // Part one
    println(s"The largest finite area: $findLargestArea")

    // Part two
    println(s"The size of the safe region: ${findSafeAreaSize(maxTotalDistance = 10000)}")
  }
}
