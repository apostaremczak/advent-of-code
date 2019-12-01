package apostaremczak.advent_of_code

import scala.io.Source
import scala.collection.immutable

/**
  * Generic puzzle interface.
  * @tparam T Puzzle input type
  */
trait Puzzle[T] {
  val day: Int

  implicit def stringToInt(s: String): Int = augmentString(s).toInt

  def input(implicit converter: String => T): immutable.List[T] =
    Source.fromResource(s"day_$day.txt").getLines().toList.map(converter)
}
