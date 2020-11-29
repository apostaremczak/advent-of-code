package com.postaremczak.advent_of_code.day_01

import org.scalatest.FunSuite

class ChronalCalibrationTest extends FunSuite {
  test("ChronalCalibration.findFinalFrequency") {
    assert(ChronalCalibration.findFinalFrequency(Seq(1, -2, 3, +1)) == 3)
    assert(ChronalCalibration.findFinalFrequency(Seq(1, 1, 1)) == 3)
    assert(ChronalCalibration.findFinalFrequency(Seq(1, 1, -2)) == 0)
    assert(ChronalCalibration.findFinalFrequency(Seq(-1, -2, -3)) == -6)
  }

  test("ChronalCalibration.findRepeatedFrequency") {
    assert(ChronalCalibration.findRepeatedFrequency(Seq(1, -1)) == 0)
    assert(ChronalCalibration.findRepeatedFrequency(Seq(3, 3, 4, -2, -4)) == 10)
    assert(ChronalCalibration.findRepeatedFrequency(Seq(-6, 3, 8, 5, -6)) == 5)
    assert(ChronalCalibration.findRepeatedFrequency(Seq(7, 7, -2, -7, -4)) == 14)
  }
}
