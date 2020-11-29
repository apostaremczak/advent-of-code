package com.postaremczak.advent_of_code.day_06

import org.scalatest.FunSuite

class ChronalCoordinatesTest extends FunSuite {
  test("ChronalCoordinates.findLargestArea") {
    assert(ChronalCoordinates.findLargestArea == 17)
  }

  test("ChronalCoordinates.findSafeAreaSize") {
    assert(ChronalCoordinates.findSafeAreaSize(maxTotalDistance = 32) == 16)
  }
}
