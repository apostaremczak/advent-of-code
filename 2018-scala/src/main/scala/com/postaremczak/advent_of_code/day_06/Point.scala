package com.postaremczak.advent_of_code.day_06

import scala.util.matching.Regex

case class Point(
                  x: Int,
                  y: Int
                ) {

  def distance(another: Point): Int = {
    (x - another.x).abs + (y - another.y).abs
  }

  def getDistances(points: Seq[Point]): Map[Point, Int] = {
    points
      .map {
        point => (point, distance(point))
      }
      .toMap
  }

  def findClosest(distances: Map[Point, Int]): Option[Point] = {
    val orderedDistances = distances
      .toSeq
      .sortWith((p1: (Point, Int), p2: (Point, Int)) => p1._2 < p2._2)

    val closest = Some(orderedDistances.head._1)
    if (orderedDistances.length >= 2) {
      // Ensure that the minimum distances are not the same
      if (orderedDistances.head._2 == orderedDistances(1)._2) {
        None
      } else {
        closest
      }
    } else {
      closest
    }
  }

  def isOnGridEdge(grid: Grid): Boolean = {
    x == grid.minX || x == grid.maxX || y == grid.minY || y == grid.maxY
  }
}


object Point {
  private val coordPattern: Regex = """(\d+), (\d+)""".r

  def apply(coordinates: String): Point = {
    coordinates match {
      case coordPattern(x, y) => Point(x.toInt, y.toInt)
    }
  }
}
