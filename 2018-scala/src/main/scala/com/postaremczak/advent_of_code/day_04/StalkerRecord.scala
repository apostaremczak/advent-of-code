package com.postaremczak.advent_of_code.day_04

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class StalkerRecord(recordTime: DateTime) {
  val time: DateTime = recordTime
}

case class NapStart(override val time: DateTime)
  extends StalkerRecord(time)

case class NapEnd(override val time: DateTime)
  extends StalkerRecord(time)

case class GuardId(override val time: DateTime, id: Int)
  extends StalkerRecord(time)

object StalkerRecord {
  private val recordPattern = """\[(\d{4}-\d{2}-\d{2} \d{2}:\d{2})\] (.*)""".r
  private val guardPattern = """(\w+) #(\d+) ((?:\w|\s)+)""".r
  private val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")

  /**
    * @param rawMessage e.g. '[1518-11-01 00:05] falls asleep'
    */
  def parse(rawMessage: String): Option[StalkerRecord] = {
    rawMessage match {
      case recordPattern(timeString, message) =>
        val time = DateTime.parse(timeString, dateTimeFormat)

        val messageType = message match {
          case "falls asleep" => NapStart(time)
          case "wakes up" => NapEnd(time)
          case guardPattern(_, guardId, _) => GuardId(time, guardId.toInt)
        }

        Some(messageType)

      case _ => None
    }
  }
}
