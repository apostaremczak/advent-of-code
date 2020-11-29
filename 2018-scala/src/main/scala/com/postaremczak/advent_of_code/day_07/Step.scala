package com.postaremczak.advent_of_code.day_07

import scala.annotation.tailrec

case class Step(
                 name: Char,
                 stepsBefore: Seq[Char]
               ) {
  def canBeExecuted(stepsTaken: String): Boolean = {
    stepsBefore.forall(s => stepsTaken.contains(s))
  }

  def execute(stepsTaken: String)(implicit instructions: Map[Char, Step]): String = {
    var instr = stepsTaken
    if (!canBeExecuted(instr)) {
      stepsBefore.foreach {
        c =>
          instr = instructions.getOrElse(c, Step(c, Seq[Char]())).execute(instr)
      }
    }

    if (instr.contains(name)) {
      instr
    } else {
      instr + name
    }
  }
}
