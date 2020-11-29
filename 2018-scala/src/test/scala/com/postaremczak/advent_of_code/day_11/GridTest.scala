package com.postaremczak.advent_of_code.day_11

import org.scalatest.FunSuite

class GridTest extends FunSuite {
  test("Grid.calculateRegionalPower") {
    assert(Grid(18).calculateRegionalPower((33, 45)) == 29)
    assert(Grid(42).calculateRegionalPower((21, 61)) == 30)
  }
}
