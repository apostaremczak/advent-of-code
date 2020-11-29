package com.postaremczak.advent_of_code.day_04

import org.scalatest.FunSuite

class ReposeRecordTest extends FunSuite {
  test("ReposeRecord.findLaziestGuard") {
    assert(ReposeRecord.findLaziestGuard == 240)
  }
  test("ReposeRecord.findFavMinuteGuard") {
    assert(ReposeRecord.findFavMinuteGuard == 4455)
  }
}
