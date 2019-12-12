package apostaremczak.advent_of_code.day_12

import apostaremczak.advent_of_code.Puzzle

import scala.annotation.tailrec

object NBodyProblem extends Puzzle[String] {
  val day = 12

  val moons: IndexedSeq[Moon] =
    input.map(position => Moon(SpaceVector(position))).toIndexedSeq

  @tailrec
  private def gcd(a: Long, b: Long): Long = if (b == 0) a.abs else gcd(b, a % b)
  private def lcm(a: Long, b: Long): Long = (a * b).abs / gcd(a, b)

  def totalSystemEnergy(moons: IndexedSeq[Moon], epochs: Int): Int = {
    val finalSystem = (1 to epochs).foldLeft(moons) {
      case (moonSystem, _) =>
        Moon.applyGravityToSystem(moonSystem)
    }

    Moon.systemEnergy(finalSystem)
  }

  def cycleLength(moons: IndexedSeq[Moon]): Long = {

    /**
      * Finds cycle length for each axis separately
      */
    @tailrec
    def axisCycleLength(
        initialState: IndexedSeq[Moon]
    )(currentState: IndexedSeq[Moon] = initialState, count: Long = 0): Long =
      if (currentState == initialState && count != 0)
        count
      else {
        axisCycleLength(initialState)(
          Moon.applyGravityToSystem(currentState),
          count + 1
        )
      }

    val xs = moons.map { moon =>
      Moon(SpaceVector(moon.position.x, 0, 0))
    }
    val xCycleLength = axisCycleLength(xs)()

    val ys = moons.map { moon =>
      Moon(SpaceVector(0, moon.position.y, 0))
    }
    val yCycleLength = axisCycleLength(ys)()

    val zs = moons.map { moon =>
      Moon(SpaceVector(0, 0, moon.position.z))
    }
    val zCycleLength = axisCycleLength(zs)()

    // Find least common multiple of those cycles
    lcm(lcm(xCycleLength, yCycleLength), zCycleLength)
  }

  def main(args: Array[String]): Unit = {
    println(s"Solution to part 1: ${totalSystemEnergy(moons, 1000)}")
    println(s"Solution to part 2: ${cycleLength(moons)}")
  }
}
