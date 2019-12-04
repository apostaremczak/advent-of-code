package apostaremczak.advent_of_code.day_03

/**
 * @param x: x position
 * @param y: y position
 */
case class Port(x: Int, y: Int) {
  def distance(that: Port): Int =
    math.abs(x - that.x) + math.abs(y - that.y)
}

object Port {
  val central: Port = Port(0, 0)
}
