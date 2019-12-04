package apostaremczak.advent_of_code.day_03

import scala.annotation.switch

case class PathComponent(direction: Direction, units: Int)

object PathComponent {
  def apply(instruction: String): PathComponent = {
    val direction = (instruction.head: @switch) match {
      case 'U' => Up
      case 'D' => Down
      case 'L' => Left
      case 'R' => Right
    }
    val units = instruction.drop(1).toInt

    PathComponent(direction, units)
  }
}
