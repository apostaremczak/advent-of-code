package apostaremczak.advent_of_code.day_05

import scala.annotation.switch

sealed trait ParameterMode

object Immediate  extends ParameterMode
object Positional extends ParameterMode

object ParameterMode {
  implicit def intToParameterMode(i: Int): ParameterMode = (i: @switch) match {
    case 0 => Positional
    case 1 => Immediate
    case unknown =>
      throw new IllegalArgumentException(s"Invalid parameter mode: $unknown")
  }
}
