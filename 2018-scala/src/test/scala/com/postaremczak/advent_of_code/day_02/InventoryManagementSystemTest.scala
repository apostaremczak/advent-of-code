package com.postaremczak.advent_of_code.day_02

import org.scalatest.FunSuite

class InventoryManagementSystemTest extends FunSuite {
  test("InventoryManagementSystem.calculateChecksum") {
    val testIds = Seq(
      "abcdef",
      "bababc",
      "abbcde",
      "abcccd",
      "aabcdd",
      "abcdee",
      "ababab"
    )
    assert(InventoryManagementSystem.calculateChecksum(testIds) == 12)
  }

  test("InventoryManagementSystem.") {
    val testIds = Seq(
      "abcde",
      "fghij",
      "klmno",
      "pqrst",
      "fguij",
      "axcye",
      "wvxyz"
    )
    assert(InventoryManagementSystem.findCorrectIdOverlap(testIds) == "fgij")
  }
}
