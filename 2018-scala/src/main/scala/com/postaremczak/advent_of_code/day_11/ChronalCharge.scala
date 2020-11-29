package com.postaremczak.advent_of_code.day_11

import com.postaremczak.advent_of_code.Solution

object ChronalCharge extends Solution(adventDay = 11) {
  val serialNumber: Int = puzzleInput.read.head.toInt
  val regionSizes: Range = Range(1, 301)

  def findMostPowerful3x3RegionTopCell(grid: Grid): (Int, Int) = {
    grid
      .cells
      .keys
      .map {
        coords: (Int, Int) =>
          (grid.calculateRegionalPower(coords), coords)
      }
      .toMap
      .max
      ._2
  }

  def findMostPowerfulRegion(grid: Grid): (Int, Int, Int) = {
    val region = grid
      .cells
      .keys
      .flatMap(
        coords =>
          regionSizes.map {
            regionSize: Int =>
              (grid.calculateRegionalPower(coords, regionSize), (coords, regionSize))
          }
      )
      .toMap
      .max
      ._2

    (region._1._1, region._1._2, region._2)
  }

  def main(args: Array[String]): Unit = {
    val grid = Grid(serialNumber)

    // Part one
    println(s"The most powerful 3x3 region's top left cell: ${findMostPowerful3x3RegionTopCell(grid)}")

    // Part one
    println(s"The most powerful region's top left cell and its size: ${findMostPowerfulRegion(grid)}")
  }
}
