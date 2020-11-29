package com.postaremczak.advent_of_code.day_02

import com.postaremczak.advent_of_code.Solution


object InventoryManagementSystem extends Solution(adventDay = 2) {

  val correctOccurrenceCount: Seq[Int] = Seq(2, 3)

  def countDoublesAndTriples(word: String): Map[Int, Char] = {
    // Drops duplicates - if more than one letter occurred 2 or 3 times,
    // only one of them will be saved
    word.toSet.toSeq
      .map { letter: Char => (word.count(_ == letter), letter) }
      .filter { letterCount => correctOccurrenceCount.contains(letterCount._1) }
      .toMap
  }

  def calculateChecksum(boxIds: Seq[String]): Int = {
    val wordScores = boxIds
      .map(countDoublesAndTriples)

    correctOccurrenceCount
      .map(score => wordScores.count(_.get(score).isDefined))
      .product
  }

  def getOverlap(id1: String, id2: String): Option[String] = {
    // Two IDs are considered correct, if they differ by only one letter
    val indexOverlap = id1.indices.filter { i: Int => id1.charAt(i) == id2.charAt(i) }
    if (indexOverlap.length == id1.length - 1) {
      Some(indexOverlap.map(id1.charAt).mkString)
    } else {
      None
    }
  }

  def findCorrectIdOverlap(boxIds: Seq[String]): String = {
    boxIds
      .map(id => boxIds.flatMap(getOverlap(id, _)))
      .filter(_.nonEmpty)
      .distinct
      .head
      .mkString
  }

  def main(args: Array[String]): Unit = {
    val boxIds: Seq[String] = puzzleInput.read

    // Part one
    println(s"Checksum: ${calculateChecksum(boxIds)}")
    // Part two
    println(s"Correct ID overlap: ${findCorrectIdOverlap(boxIds)}")

    puzzleInput.stream.close()
  }
}
