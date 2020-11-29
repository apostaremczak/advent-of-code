package com.postaremczak.advent_of_code.day_05

import org.scalatest.FunSuite

class AlchemicalReductionTest extends FunSuite {
  test("AlchemicalReductionTest.willUnitsReact") {
    assert(!AlchemicalReduction.willUnitsReact('a', 'a'))
    assert(!AlchemicalReduction.willUnitsReact('a', 'B'))
    assert(AlchemicalReduction.willUnitsReact('a', 'A'))
    assert(AlchemicalReduction.willUnitsReact('B', 'b'))
  }

  test("AlchemicalReduction.scanPolymer") {
    assert(AlchemicalReduction.scanPolymer(toBeScanned = "dabAcCaCBAcCcaDA") == "dabCBAcaDA")
  }

  test("AlchemicalReduction.findShortestPolymer") {
    assert(AlchemicalReduction.findShortestPolymer("dabAcCaCBAcCcaDA") == 4)
  }
}
