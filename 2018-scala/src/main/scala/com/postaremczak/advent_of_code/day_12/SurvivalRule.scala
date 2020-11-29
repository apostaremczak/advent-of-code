package com.postaremczak.advent_of_code.day_12

case class SurvivalRule(
                         surroundings: Seq[Pot],
                         effect: Pot
                       )

object SurvivalRule {
  private val survivalRulePattern = """([.#]+) => ([.#])""".r

  def findSurvivalRules(rawRules: Seq[String]): Seq[SurvivalRule] = {
    rawRules
      .map {
        rule: String =>
          rule match {
            case survivalRulePattern(surroundings, effect) =>
              SurvivalRule(surroundings.map(Pot(_)), Pot(effect.head))
          }
      }
  }

  def decide(fivePots: Seq[Pot])(implicit rules: Seq[SurvivalRule]): Pot = {
    rules
      .find(_.surroundings.equals(fivePots))
      .getOrElse(SurvivalRule(fivePots, Pot.empty))
      .effect
  }
}
