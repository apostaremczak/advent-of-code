package com.postaremczak.advent_of_code.day_11

import org.scalatest.FunSuite

class ChronalChargeTest extends FunSuite {
  val grid1 = Grid(18)
  val grid2 = Grid(42)

  test("ChronalChargeTest.findMostPowerful3x3RegionTopCell") {
   assert(ChronalCharge.findMostPowerful3x3RegionTopCell(grid1) == (33, 45))
   assert(ChronalCharge.findMostPowerful3x3RegionTopCell(grid2) == (21, 61))
 }

  test("ChronalChargeTest.findMostPowerfulRegion") {
    assert(ChronalCharge.findMostPowerfulRegion(grid1) == (90, 269, 16))
    assert(ChronalCharge.findMostPowerfulRegion(grid2) == (232, 251, 12))
  }
}
