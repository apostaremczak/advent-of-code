package com.postaremczak.advent_of_code.day_07

import com.postaremczak.advent_of_code.Solution
import scala.collection.immutable.ListMap

object TheSumOfItsParts extends Solution(adventDay = 7) {

  private val stepPattern = """Step (\w) must be finished before step (\w) can begin\.""".r
  lazy val steps: Map[Char, Step] = parseInstructions(puzzleInput.read)

  def parseInstructions(instructions: Seq[String]): Map[Char, Step] = {
    ListMap(
      instructions
        .map {
          instruction: String =>
            instruction match {
              case stepPattern(stepBefore, step) => (stepBefore.head, step.head)
            }
        }
        .groupBy(_._2)
        .mapValues(_.map(_._1))
        .map {
          case (stepName: Char, stepsBefore: Seq[Char]) =>
            (stepName, Step(stepName, stepsBefore))
        }
        .toSeq
        .sortWith((s1: (Char, Step), s2: (Char, Step)) => s1._1 < s2._1): _*
    )
  }

  def findStepOrder: String = {
    println(s"Keys: ${steps.keys.mkString}")
    // Find the step which has no steps to be executed before
    val firstStep = steps
      .flatMap(_._2.stepsBefore)
      .toSeq
      .filter((s: Char) => !steps.contains(s))
      .head

    var instruction = firstStep.toString
    steps
      .foreach {
        s =>
          println(s"Current instruction: $instruction")
          instruction = s._2.execute(instruction)(steps)
      }
    instruction
  }

  def main(args: Array[String]): Unit = {
    // Part one
    println(s"Instruction order: $findStepOrder")
  }
}
