package com.postaremczak.advent_of_code.day_12

import org.scalatest.FunSuite

class SubterraneanSustainabilityTest extends FunSuite {
  test("SubterraneanSustainability.firstGeneration") {
    assert(SubterraneanSustainability.evolveToFutureGeneration(1).mkString == ".#...#....#.....#..#..#..#.")
  }

  test("SubterraneanSustainability.sumOfFuturePotIndices") {
    assert(SubterraneanSustainability.sumOfFuturePotIndices(20) == 325)
  }
}
