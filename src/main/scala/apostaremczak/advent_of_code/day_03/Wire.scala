package apostaremczak.advent_of_code.day_03

case class Wire(path: IndexedSeq[Port]) {

  /**
    * While the wires do technically cross right at the central port where they both start,
    * this point does not count, nor does a wire count as crossing with itself.
    */
  def intersect(that: Wire): Set[Port] =
    path.toSet.intersect(that.path.toSet).diff(Set(Port.central))
}

object Wire {
  def addPathComponent(
      path: IndexedSeq[Port],
      pathComponent: PathComponent
  ): IndexedSeq[Port] = {
    val current = path.last

    val newPorts = pathComponent.direction match {
      case Up =>
        (current.y + 1 to current.y + pathComponent.units)
          .map(Port(current.x, _))

      case Down =>
        (current.y - 1 to current.y - pathComponent.units by -1)
          .map(Port(current.x, _))

      case Left =>
        (current.x - 1 to current.x - pathComponent.units by -1)
          .map(Port(_, current.y))

      case Right =>
        (current.x + 1 to current.x + pathComponent.units)
          .map(Port(_, current.y))
    }

    path ++ newPorts
  }

  def apply(instructions: List[String]): Wire = {
    val path = instructions
      .map(PathComponent(_))
      .foldLeft(IndexedSeq(Port.central))(addPathComponent)

    Wire(path)
  }
}
