package apostaremczak.advent_of_code.day_12

import apostaremczak.advent_of_code.Puzzle

object NBodyProblem extends Puzzle[String] {
  val day = 12

  val moons: IndexedSeq[Moon] =
    input.map(position => Moon(SpaceVector(position))).toIndexedSeq

  def totalSystemEnergy(moons: IndexedSeq[Moon], epochs: Int): Int = {
    val finalSystem = (1 to epochs).foldLeft(moons) {
      case (moonSystem, _) =>
        Moon.applyGravityToSystem(moonSystem)
    }

    Moon.systemEnergy(finalSystem)
  }

  def cycleLength(moons: IndexedSeq[Moon]): Int = {
    ???
  }

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: ${totalSystemEnergy(moons, 1000)}")
  }
}
