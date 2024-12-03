package io.github.coden.impulse.judge

import io.github.coden.impulse.judge.Match.Companion.ifFailed
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class TimeRule(): Rule<LocalDateTime> {

    private val workTimeFrom = LocalTime.of(9, 0)
    private val workTimeTo = LocalTime.of(18, 0)

    override fun test(entity: LocalDateTime): Match {
        return (isWork() or isChillTime()).invoke(entity)
    }


    private inline infix fun <T> ((T) -> Match).and(crossinline other: (T) -> Match): (T) -> Match {
        return { this(it) and other(it) }
    }


    private inline infix fun <T> ((T) -> Match).or(crossinline other: (T) -> Match): (T) -> Match {
        return { this(it) or other(it) }
    }

    private fun isWork(): (LocalDateTime) -> Match = (isWorkTime() and isWorkDay())

    private fun isWorkTime(): (LocalDateTime) -> Match = {
         (!it.toLocalTime().isAfter(workTimeFrom) && !it.toLocalTime().isBefore(workTimeTo)).ifFailed(
             "Not a work time"
         )
    }

    private fun isWorkDay(): (LocalDateTime) -> Match = {
        (it.dayOfWeek != DayOfWeek.SATURDAY
                && it.dayOfWeek != DayOfWeek.SUNDAY
                && it.dayOfWeek != DayOfWeek.FRIDAY).ifFailed(
                    "Not a work day"
                )
    }

    private fun isChillTime(): (LocalDateTime) -> Match = {
        (it.dayOfWeek in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY))
            .ifFailed("Not a chill time")
    }

}