package apostaremczak.advent_of_code.day_12

case class SpaceVector(x: Int, y: Int, z: Int) {
  def +(that: SpaceVector): SpaceVector =
    SpaceVector(x + that.x, y + that.y, z + that.z)

  def energy: Int =
    math.abs(x) + math.abs(y) + math.abs(z)
}

object SpaceVector {
  def apply(position: String): SpaceVector = {
    val pattern = "<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>".r
    val pattern(x, y, z) = position

    SpaceVector(x.toInt, y.toInt, z.toInt)
  }
}
