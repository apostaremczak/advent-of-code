package com.postaremczak.advent_of_code.day_12

case class Pot(
                hasPlant: Boolean
              )


object Pot {
  def apply(potSign: Char): Pot = {
    potSign match {
      case '#' => Pot(true)
      case '.' => Pot(false)
      case _ => throw new RuntimeException(s"Invalid character found in plant surviving rules: $potSign")
    }
  }

  def empty: Pot = Pot(false)
}
