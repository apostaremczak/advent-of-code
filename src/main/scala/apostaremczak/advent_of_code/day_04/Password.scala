package apostaremczak.advent_of_code.day_04

case class Password(text: String) {
  def isCorrectLength: Boolean = text.length == 6

  def isWithinRange(rangeStart: Int, rangeEnd: Int): Boolean =
    rangeStart <= text.toInt && text.toInt <= rangeEnd

  def hasDoubleRepetition: Boolean =
    text.toSeq.sliding(2, 1).map(_.unwrap).exists { _.toSet.size == 1 }

  def hasOnlyDoubleRepetitions: Boolean = { ??? }

  def isMonotonic: Boolean =
    text.toSeq.sliding(2, 1).map(_.unwrap).forall { pair: String =>
      val Array(first, second) = pair.toArray.map(_.asDigit)
      first <= second
    }

  /**
    * It is a six-digit number.
    * The value is within the range given in your puzzle input.
    * Two adjacent digits are the same (like 22 in 122345).
    * Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
    */
  def isPartiallyValid(rangeStart: Int, rangeEnd: Int): Boolean =
    isCorrectLength && isWithinRange(rangeStart, rangeEnd) && hasDoubleRepetition && isMonotonic

  /**
    * An Elf just remembered one more important detail: the two adjacent matching digits are not part
    * of a larger group of matching digits.
    */
  def isValid(rangeStart: Int, rangeEnd: Int): Boolean =
    isCorrectLength && isWithinRange(rangeStart, rangeEnd) && hasOnlyDoubleRepetitions && isMonotonic
}
