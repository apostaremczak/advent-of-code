package apostaremczak.advent_of_code.day_04

import apostaremczak.advent_of_code.Puzzle

object SecureContainer extends Puzzle[String] {
  val day = 4

  val rangeStart: Int = 235741
  val rangeEnd: Int   = 706948

  /**
    * Part 1: More than two repetitions are accepted.
    */
  def countPartiallyValidPasswords: Int = (rangeStart to rangeEnd).count {
    pass =>
      Password(pass.toString).isPartiallyValid(rangeStart, rangeEnd)
  }

  /**
    * Part 2: Only two repetitions are accepted.
    */
  def countValidPasswords: Int = (rangeStart to rangeEnd).count { pass =>
    Password(pass.toString).isValid(rangeStart, rangeEnd)
  }

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: $countPartiallyValidPasswords")
    println(s"Solution to part 2: $countValidPasswords")
  }
}
