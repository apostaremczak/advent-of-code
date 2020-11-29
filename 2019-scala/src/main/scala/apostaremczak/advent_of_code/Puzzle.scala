package apostaremczak.advent_of_code

import scala.io.Source
import scala.collection.immutable
import scala.reflect.ClassTag

/**
  * Generic puzzle interface.
  * @tparam T Puzzle input type
  */
trait Puzzle[T] {
  val day: Int

  implicit def stringToInt(s: String): Int = augmentString(s).toInt

  def rawInput: List[String] =
    Source.fromResource(s"day_$day.txt").getLines().toList

  def input(
      implicit converter: String => T,
      classTag: ClassTag[T]
  ): immutable.List[T] =
    rawInput.map(converter)
}

trait CommaSeparatedPuzzle[T] extends Puzzle[T] {
  override def input(
      implicit converter: String => T,
      classTag: ClassTag[T]
  ): List[T] = rawInput.flatMap(_.split(",").map(converter))
}
