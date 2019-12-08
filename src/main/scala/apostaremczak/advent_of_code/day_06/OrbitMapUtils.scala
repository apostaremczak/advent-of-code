package apostaremczak.advent_of_code.day_06

object OrbitMapUtils {
  // Map of names and space objects
  type OrbitMap = Map[String, SpaceObject]

  def startOrbitMap: OrbitMap = Map(
    "COM" -> SpaceObject(None)
  )

  /**
    *
    * @param orbitMap Current orbit map
    * @param spaceObjectRecipe Orbit description in format `AAA)BBB`
    * @return Updated orbit map
    */
  private def addToOrbitMap(
      orbitMap: OrbitMap,
      spaceObjectRecipe: String
  ): OrbitMap = {
    val Array(parentName, childName) = spaceObjectRecipe split ')'

    // Check if the parent has already been mentioned, if not, add it to the orbit map
    val orbitMapWithParent = orbitMap get parentName match {
      case Some(_) => orbitMap
      case None         =>
        // Create a parent placeholder and add to orbit map
        val parent = SpaceObject(None)
        orbitMap updated (parentName, parent)
    }

    // Add this object to the map
    orbitMapWithParent updated (childName, SpaceObject(Some(parentName)))
  }

  def parseMapRecipes(spaceObjectRecipes: List[String]): OrbitMap =
    spaceObjectRecipes.foldLeft(startOrbitMap)(addToOrbitMap)
}
