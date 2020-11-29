package com.postaremczak.advent_of_code.day_05

import com.postaremczak.advent_of_code.Solution

import scala.annotation.tailrec

object AlchemicalReduction extends Solution(adventDay = 5) {
  val polymer: String = puzzleInput.read.head

  def willUnitsReact(u1: Char, u2: Char): Boolean = {
    u1.toLower == u2.toLower && Seq(u1, u2).exists(_.isLower) && Seq(u1, u2).exists(_.isUpper)
  }

  @tailrec
  def scanPolymer(toBeScanned: String, alreadyScanned: String = ""): String = {
    if (toBeScanned.isEmpty) {
      // Base case:
      // Nothing's left to be scanned, no more reactions will occur
      alreadyScanned
    } else {
      val next = toBeScanned.head
      if (alreadyScanned.isEmpty) {
        scanPolymer(toBeScanned.drop(1), next.toString)
      } else {
        // Look for any possible reactions
        val prev = alreadyScanned.last
        if (willUnitsReact(prev, next)) {
          scanPolymer(toBeScanned.drop(1), alreadyScanned.dropRight(1))
        } else {
          scanPolymer(toBeScanned.drop(1), alreadyScanned + next)
        }
      }
    }
  }

  def findReducedPolymerLength(polymer: String): Int = {
    scanPolymer(toBeScanned = polymer).length
  }

  def findShortestPolymer(polymer: String): Int = {
    val availableUnits = polymer.toLowerCase.toSet

    availableUnits
      .map {
        unit: Char =>
          val polymerWithRemovedUnit = polymer.filter(_.toLower != unit)
          findReducedPolymerLength(polymerWithRemovedUnit)
      }
      .min
  }

  def main(args: Array[String]): Unit = {
    // Part one
    println(s"Boom! The whole polymer has reacted and is now reduced to ${findReducedPolymerLength(polymer)} units")

    // Part two
    println(s"The shortest possible polymer has ${findShortestPolymer(polymer)} units")
  }
}
