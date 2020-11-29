package com.postaremczak.advent_of_code.day_11

import org.scalatest.FunSuite

class CellTest extends FunSuite {

  test("Cell.calculatePower") {
    assert(Cell(3, 5)(serialNumber = 8).power == 4)
    assert(Cell(122, 79)(serialNumber = 57).power == -5)
    assert(Cell(217, 196)(serialNumber = 39).power == 0)
    assert(Cell(101, 153)(serialNumber = 71).power == 4)
  }
}
