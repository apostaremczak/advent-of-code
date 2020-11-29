package com.postaremczak.advent_of_code.day_04

import org.joda.time.DateTime

case class Shift(
                       guardId: Int,
                       sleepingMinutes: Seq[Int],
                       sleepingTime: Int
                     )

object Shift {
  private def getNappingMinutes(start: DateTime, end: DateTime): Seq[Int] = {
    Range(start.getMinuteOfHour, end.getMinuteOfHour)
  }

  def extract(
               recordsRemaining: Seq[StalkerRecord],
               nappingMinutes: Seq[Int] = Seq(),
               sleepingTime: Int = 0,
               currentGuardId: Option[Int] = None,
               shiftRecords: Seq[Shift] = Seq()
             ): Seq[Shift] = {

    if (recordsRemaining.isEmpty) {
      // Extracting shifts has ended
      shiftRecords

    } else {
      recordsRemaining.head match {
        case guard: GuardId =>
          // A shift has ended, start a new one
          if (currentGuardId.isDefined) {
            val newGuardShift = Shift(currentGuardId.get, nappingMinutes, sleepingTime)

            extract(
              recordsRemaining = recordsRemaining.drop(1),
              currentGuardId = Some(guard.id),
              shiftRecords = shiftRecords.union(Seq(newGuardShift))
            )

          } else {
            // No shift has been extracted before
            extract(
              recordsRemaining = recordsRemaining.drop(1),
              currentGuardId = Some(guard.id)
            )
          }

        // A guard has fallen asleep
        case start: NapStart =>
          val minutes: Seq[Int] = getNappingMinutes(start.time, recordsRemaining(1).time)

          extract(
            recordsRemaining = recordsRemaining.drop(2),
            currentGuardId = currentGuardId,
            nappingMinutes = nappingMinutes.union(minutes),
            sleepingTime = sleepingTime + minutes.last + 1 - minutes.head,
            shiftRecords = shiftRecords
          )
      }
    }
  }
}
