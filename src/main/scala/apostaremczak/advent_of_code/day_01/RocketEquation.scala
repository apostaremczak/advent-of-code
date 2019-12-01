package apostaremczak.advent_of_code.day_01

import apostaremczak.advent_of_code.Puzzle

object RocketEquation extends Puzzle[Int] {
  val day = 1

  /**
    * Fuel required to launch a given module is based on its mass.
    * Specifically, to find the fuel required for a module,
    * take its mass, divide by three, round down, and subtract 2.
    */
  def moduleFuelRequired(moduleMass: Int): Int = (moduleMass / 3) - 2

  /**
    * What is the sum of the fuel requirements for all of the modules on your spacecraft?
    */
  def fuelRequiredForModules: Int =
    input.map(moduleFuelRequired).sum

  /**
    * Fuel itself requires fuel just like a module - take its mass,
    * divide by three, round down, and subtract 2. However, that
    * fuel also requires fuel, and that fuel requires fuel, and so on.
    * Any mass that would require negative fuel should instead be treated as
    * if it requires zero fuel; the remaining mass, if any, is instead
    * handled by wishing really hard, which has no mass and is outside
    * the scope of this calculation.
    */
  def recursiveFuelRequired(fuel: Int): Int = {
    val modulesFuel = moduleFuelRequired(fuel)

    if (modulesFuel <= 0)
      0
    else
      modulesFuel + recursiveFuelRequired(modulesFuel)
  }

  /**
    * What is the sum of the fuel requirements for all of the modules on your
    * spacecraft when also taking into account the mass of the added fuel?
    */
  def totalFuelRequired: Int =
    input.map(recursiveFuelRequired).sum

  def main(args: Array[String]): Unit = {
    println(s"Part 1 solution: $fuelRequiredForModules")
    println(s"Part 2 solution: $totalFuelRequired")
  }
}
