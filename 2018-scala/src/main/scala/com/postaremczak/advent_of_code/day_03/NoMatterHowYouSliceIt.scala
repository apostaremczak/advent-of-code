package com.postaremczak.advent_of_code.day_03

import com.postaremczak.advent_of_code.Solution


object NoMatterHowYouSliceIt extends Solution(adventDay = 3) {
  val fabricClaims: Seq[FabricClaim] = puzzleInput.read.flatMap(FabricClaim(_))

  lazy val overlappingSquares: Seq[(Int, Int)] = fabricClaims
    .flatMap(_.squares)
    .groupBy(identity)
    .mapValues(_.size)
    .filter(_._2 > 1)
    .keys
    .toSeq

  def countOverlappingSquares: Int = {
    overlappingSquares.size
  }

  def findNotOverlappingClaimId: Int = {
    val overlappingSet = overlappingSquares.toSet

    fabricClaims
      .map { fabricClaim => (fabricClaim.id, fabricClaim.squares) }
      .filter(_._2.toSet.intersect(overlappingSet).isEmpty)
      .head
      ._1
  }

  def main(args: Array[String]): Unit = {
    // Part one
    println(s"Number of overlapping squares: $countOverlappingSquares")

    // Part two
    println(s"ID of the only not overlapping claim: $findNotOverlappingClaimId")

    puzzleInput.stream.close()
  }
}
