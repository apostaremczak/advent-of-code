package com.postaremczak.advent_of_code.day_11


case class Grid(serialNumber: Int) {

  lazy val cells: Map[(Int, Int), Cell] = {
    Range(1, 301)
      .flatMap(
        x =>
          Range(1, 301)
            .map(
              y => ((x, y), Cell(x, y)(serialNumber))
            )
      )
      .toMap
  }

  def calculateRegionalPower(topLeftCoords: (Int, Int), regionSize: Int = 3): Int = {
    // Region regionSize: 3x3
    val (minX, minY) = topLeftCoords

    Range(minX, minX + regionSize)
      .flatMap(
        x =>
          Range(minY, minY + regionSize)
            .map(
              y =>
                cells.get((x, y)) match {
                  case Some(cell) => cell.power
                  case None => 0
                }
            )
      )
      .sum
  }
}
