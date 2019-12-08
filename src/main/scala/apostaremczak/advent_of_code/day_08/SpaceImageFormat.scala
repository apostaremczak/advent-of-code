package apostaremczak.advent_of_code.day_08

import apostaremczak.advent_of_code.Puzzle

object SpaceImageFormat extends Puzzle[String] {
  val day = 8

  val biosPasswordImage = SpaceImage(25, 6, input.head)

  /**
    * To make sure the image wasn't corrupted during transmission,
    * the Elves would like you to find the layer that contains
    * the fewest 0 digits. On that layer, what is the number of
    * 1 digits multiplied by the number of 2 digits?
    */
  def verifyImageEncoding(spaceImage: SpaceImage): Int = {
    val occurrenceCount = spaceImage.layers
      .minBy(_.count(_ == 0))
      .groupBy(identity)
      .map {
        case (digit, digitSeq) =>
          (digit, digitSeq.size)
      }

    occurrenceCount.getOrElse(1, 0) * occurrenceCount.getOrElse(2, 0)
  }

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: ${verifyImageEncoding(biosPasswordImage)}")
    println(s"Solution to part 2:\n${biosPasswordImage.render}")
  }
}
