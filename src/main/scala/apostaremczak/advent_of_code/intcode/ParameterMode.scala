package apostaremczak.advent_of_code.intcode

import scala.annotation.switch
import scala.language.implicitConversions

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
