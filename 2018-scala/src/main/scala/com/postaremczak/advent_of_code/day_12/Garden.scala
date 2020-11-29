package com.postaremczak.advent_of_code.day_12


case class Garden(
                   alignment: Map[Int, Pot]
                 )(
                   implicit survivalRules: Seq[SurvivalRule]
                 ) {

  def getPot(potIndex: Int): Pot = alignment.getOrElse(potIndex, Pot.empty)

  def getSurroundings(potIndex: Int): Seq[Pot] = {
    Range(potIndex - 2, potIndex + 3).map(getPot)
  }

  def evaluateGeneration: Garden = {
    // Check whether the garden's borders can be expanded
    val toTheLeft = alignment.keys.min - 1
    val toTheRight = alignment.keys.max + 1

    Garden(
      alignment
        .updated(toTheLeft, Pot.empty)
        .updated(toTheRight, Pot.empty)
        .keys
        .map {
          potIndex: Int => (potIndex, SurvivalRule.decide(getSurroundings(potIndex)))
        }
        .toMap
    )
  }

  def mkString: String = {
    alignment
      .toSeq
      .sortWith((p1, p2) => p1._1 < p2._1)
      .map { p => if (p._2.hasPlant) "#" else "." }
      .mkString(sep = "")
  }
}


object Garden {

  private val initialStatePattern = """initial state: ([.#]+)""".r

  def apply(input: Seq[String]): Garden = {
    val firstGeneration = input.head match {
      case initialStatePattern(pots) =>
        pots
          .indices
          .map {
            i => (i, Pot(pots.charAt(i)))
          }
          .toMap
    }
    implicit val survivalRules: Seq[SurvivalRule] = SurvivalRule.findSurvivalRules(input.drop(2))

    Garden(firstGeneration)
  }
}
