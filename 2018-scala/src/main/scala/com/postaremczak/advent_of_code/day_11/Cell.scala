package com.postaremczak.advent_of_code.day_11


case class Cell(
                 x: Int,
                 y: Int
               )(
                 implicit serialNumber: Int
               ) {
  lazy val power: Int = calculatePower

  private def calculatePower: Int = {
    def getHundreds(n: Int): Int = {
      (n / 100) % 10
    }

    val rackId = x + 10
    getHundreds((rackId * y + serialNumber) * rackId) - 5
  }
}
