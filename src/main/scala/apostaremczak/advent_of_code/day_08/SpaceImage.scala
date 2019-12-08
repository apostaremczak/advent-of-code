package apostaremczak.advent_of_code.day_08

import scala.annotation.switch

case class SpaceImage(
    width: Int,
    height: Int,
    layers: IndexedSeq[SpaceImage.Layer]
) {
  private def resolveColor(prevColor: Int, currentColor: Int): Int =
    (prevColor: @switch) match {
      case 2              => currentColor
      case nonTransparent => nonTransparent
    }

  /**
    * The image is rendered by stacking the layers and aligning
    * the pixels with the same positions in each layer.
    * The digits indicate the color of the corresponding pixel:
    * - 0 is black,
    * - 1 is white,
    * - 2 is transparent.
    */
  def render: String = {
    // Group pixels of all layers by their positions
    layers
      .flatMap(_.zipWithIndex)
      .groupBy(_._2)
      .map {
        case (position, pixels) =>
          // Resolve color at each pixel position
          (position, pixels.map(_._1).foldLeft(2)(resolveColor))
      }
      .toList
      .sortBy(_._1)
      .map(_._2 match {
        case 1 => "█"
        case 0 => " "
      })
      .grouped(width)
      .map(_.mkString)
      .mkString("\n")
  }
}

object SpaceImage {
  type Layer = IndexedSeq[Int]

  def apply(width: Int, height: Int, encoding: String): SpaceImage = {
    val layerLength = width * height
    val layers = encoding.toSeq
      .sliding(size = layerLength, step = layerLength)
      .map { layer =>
        layer.map(_.asDigit)
      }
      .toIndexedSeq

    SpaceImage(width, height, layers)
  }
}
