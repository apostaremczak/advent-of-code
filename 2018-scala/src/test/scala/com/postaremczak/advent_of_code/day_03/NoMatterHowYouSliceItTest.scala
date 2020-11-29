package com.postaremczak.advent_of_code.day_03

import org.scalatest.FunSuite

class NoMatterHowYouSliceItTest extends FunSuite {
  test("NoMatterHowYouSliceIt.countOverlappingSquares") {
    assert(NoMatterHowYouSliceIt.countOverlappingSquares == 4)
  }

  test("NoMatterHowYouSliceIt.findNotOverlappingClaimId") {
    assert(NoMatterHowYouSliceIt.findNotOverlappingClaimId == 3)
  }
}
