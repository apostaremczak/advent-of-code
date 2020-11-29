package com.postaremczak.advent_of_code.day_04

import com.postaremczak.advent_of_code.{PuzzleInput, Solution}

object ReposeRecord extends Solution(adventDay = 4) {

  lazy val guards: Seq[Guard] = getGuards(puzzleInput)

  def getGuards(puzzleInput: PuzzleInput): Seq[Guard] = {
    val records: Seq[StalkerRecord] = puzzleInput
      .read
      .flatMap(StalkerRecord.parse)
      .sortWith((r1, r2) => r1.time.isBefore(r2.time))

    // Summarise guards and their favourite napping time based on the provided records
    Shift
      .extract(records)
      .groupBy(_.guardId)
      .mapValues {
        shifts: Seq[Shift] =>
          (shifts.flatMap(_.sleepingMinutes).sorted, shifts.map(_.sleepingTime).sum)
      }
      .flatMap {
        case (guardId: Int, (sleepingMinutes: Seq[Int], napTime: Int)) =>
          // Some guards manage to do all their shifts without a single nap! :o
          if (sleepingMinutes.nonEmpty) {
            // Retrieve a minute at which a guard is asleep the most often
            val favouriteMinute = sleepingMinutes
              .distinct
              .map { minute: Int => (sleepingMinutes.count(_ == minute), minute) }
              .toMap
              .max

            Some(
              Guard(
                id = guardId,
                totalNapTime = napTime,
                favouriteNappingMinute = favouriteMinute._2,
                favouriteMinuteCount = favouriteMinute._1
              )
            )
          } else {
            None
          }
      }
      .toSeq
  }

  def findWinner(sorting: (Guard, Guard) => Boolean): Int = {
    val winner = guards
      .sortWith(sorting)
      .last

    winner.id * winner.favouriteNappingMinute
  }

  def findLaziestGuard: Int = {
    findWinner((g1, g2) => g1.totalNapTime < g2.totalNapTime)
  }

  def findFavMinuteGuard: Int = {
    findWinner((g1, g2) => g1.favouriteMinuteCount < g2.favouriteMinuteCount)
  }

  def main(args: Array[String]): Unit = {
    // Part one
    println(s"ID of the laziest guard multiplied by the most popular sleeping minute: $findLaziestGuard")

    // Part two
    println(s"ID of the guard which is most frequently asleep on the same minute multiplied by that minute: $findFavMinuteGuard}")

    puzzleInput.stream.close()
  }
}
