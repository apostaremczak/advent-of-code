package com.postaremczak.advent_of_code.day_01

import com.postaremczak.advent_of_code.Solution


object ChronalCalibration extends Solution(adventDay = 1) {

  def findFinalFrequency(frequencyInput: Seq[Int]): Int = {
    frequencyInput.sum
  }

  def findRepeatedFrequency(frequencyInput: Seq[Int], frequenciesSeen: Set[Int] = Set(0), currentFrequency: Int = 0): Int = {
    val nextFrequency = currentFrequency + frequencyInput.head
    if (frequenciesSeen.contains(nextFrequency)) {
      nextFrequency
    } else {
      findRepeatedFrequency(
        frequencyInput.drop(1).union(Seq(frequencyInput.head)),
        frequenciesSeen.union(Set(nextFrequency)),
        nextFrequency
      )
    }
  }

  def main(args: Array[String]): Unit = {
    val frequencyInput: Seq[Int] = puzzleInput.read.map(_.toInt)

    // Part one
    println(s"Last frequency: ${findFinalFrequency(frequencyInput)}")
    // Part two
    println(s"First repeated frequency: ${findRepeatedFrequency(frequencyInput)}")

    puzzleInput.stream.close()
  }
}
