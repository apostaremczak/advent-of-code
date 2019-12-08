package apostaremczak.advent_of_code.day_08

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SpaceImageFormatSpec extends AnyWordSpec with Matchers {
  val twoLayerImage = SpaceImage(
    width = 3,
    height = 2,
    layers = IndexedSeq(
      IndexedSeq(1, 2, 3, 4, 5, 6),
      IndexedSeq(7, 8, 9, 0, 1, 2)
    )
  )

  "SpaceImage" must {
    "be created from encoded message" in {
      val message = "123456789012"
      SpaceImage(3, 2, message) must equal(twoLayerImage)
    }

    "render non-transparent colors" in {
      val smallImage = SpaceImage(2, 2, "0222112222120000")
      val expectedMessage = " █\n█ "
      smallImage.render must equal(expectedMessage)
    }
  }

  "SpaceImageFormat" must {
    "verify image encoding" in {
      SpaceImageFormat.verifyImageEncoding(twoLayerImage) must equal(1)
    }
  }
}
