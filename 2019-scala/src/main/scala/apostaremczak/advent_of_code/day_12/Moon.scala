package apostaremczak.advent_of_code.day_12

case class Moon(
    position: SpaceVector,
    velocity: SpaceVector = SpaceVector(0, 0, 0)
) {
  private def axisGravity(axis1: Int, axis2: Int): Int =
    math.signum(axis2 - axis1)

  def applyGravityEffect(that: Moon): SpaceVector =
    SpaceVector(
      axisGravity(position.x, that.position.x),
      axisGravity(position.y, that.position.y),
      axisGravity(position.z, that.position.z)
    )

  def potentialEnergy: Int =
    position.energy

  def kineticEnergy: Int =
    velocity.energy

  def totalEnergy: Int =
    potentialEnergy * kineticEnergy
}

object Moon {

  /**
    * Calculate new velocities of a system and add them to previous positions.
    */
  def applyGravityToSystem(moons: IndexedSeq[Moon]): IndexedSeq[Moon] =
    moons.zipWithIndex
      .map {
        case (moon: Moon, index: Int) =>

          // For each moon, add gravity effects of all the other moons
          val newVelocity = moons.zipWithIndex
            .filter(_._2 != index)
            .map(_._1)
            .foldLeft(moon.velocity) {
              (gravityEffect: SpaceVector, otherMoon: Moon) =>
                gravityEffect + moon.applyGravityEffect(otherMoon)
            }

          // Update moon's velocity and position
          Moon(moon.position + newVelocity, newVelocity)
      }

  def systemEnergy(moons: IndexedSeq[Moon]): Int =
    moons.map(_.totalEnergy).sum
}
