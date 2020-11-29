package com.postaremczak.advent_of_code

import java.io.InputStream

import scala.io.Source

case class PuzzleInput(adventDay: Int) {
  val fileName = s"day_${if (adventDay / 10 >= 1) adventDay else "0" + adventDay}_input.txt"
  val stream: InputStream = getClass.getResourceAsStream(s"/$fileName")

  def read: Seq[String] = Source.fromInputStream(stream).getLines().toSeq
}
