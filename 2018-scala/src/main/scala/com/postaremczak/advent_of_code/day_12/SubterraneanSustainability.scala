package com.postaremczak.advent_of_code.day_12

import com.postaremczak.advent_of_code.Solution

import scala.annotation.tailrec

object SubterraneanSustainability extends Solution(adventDay = 12) {
  val garden = Garden(puzzleInput.read)

  def evolveToFutureGeneration(numGenerations: Long): Garden = {

    @tailrec
    def evaluate(currentGarden: Garden, numToEvaluate: Long, currentCount: Long = 0): Garden = {
      if (currentCount == numToEvaluate) {
        currentGarden
      } else {
        evaluate(currentGarden.evaluateGeneration, numToEvaluate, currentCount + 1)
      }
    }

    evaluate(garden, numGenerations)
  }

  def sumOfFuturePotIndices(numGenerations: Long): Long = {
    val futureGarden = evolveToFutureGeneration(numGenerations)
    futureGarden
      .alignment
      .map {
        case (index: Int, pot: Pot) =>
          if (pot.hasPlant) {
            index
          } else {
            0
          }
      }
      .sum
  }


  def main(args: Array[String]): Unit = {
    // Part one
    println(s"The sum of the numbers of all pots which contain a plant in 20th generation: ${sumOfFuturePotIndices(20)}")
  }

  // Part two
//  println(s"The sum of the numbers of all pots which contain a plant in 20th generation: ${sumOfFuturePotIndices(50000000000L)}")
}
