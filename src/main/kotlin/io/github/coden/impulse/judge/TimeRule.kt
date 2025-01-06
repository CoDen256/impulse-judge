package io.github.coden.impulse.judge

import io.github.coden.impulse.judge.Match.Companion.ifFailed
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class TimeRule(): Rule<LocalDateTime> {

    private val workTimeFrom = LocalTime.of(9, 0)
    private val workTimeTo = LocalTime.of(16, 0)

    override fun test(entity: LocalDateTime): Match {
        return (isWork() or isScheduled()).invoke(entity)
    }


    private inline infix fun <T> ((T) -> Match).and(crossinline other: (T) -> Match): (T) -> Match {
        return { this(it) and other(it) }
    }


    private inline infix fun <T> ((T) -> Match).or(crossinline other: (T) -> Match): (T) -> Match {
        return { this(it) or other(it) }
    }

    private fun isWork(): (LocalDateTime) -> Match = (isWorkTime() and isWorkDay())

    private fun isWorkTime(): (LocalDateTime) -> Match = {
         (!it.toLocalTime().isBefore(workTimeFrom) && !it.toLocalTime().isAfter(workTimeTo)).ifFailed(
             "Not a work time (09:00-16:00)"
         )
    }

    private fun isWorkDay(): (LocalDateTime) -> Match = {
        (it.dayOfWeek != DayOfWeek.SATURDAY
                && it.dayOfWeek != DayOfWeek.MONDAY
                && it.dayOfWeek != DayOfWeek.SUNDAY
                && it.dayOfWeek != DayOfWeek.FRIDAY).ifFailed(
                    "Not a home office work day (tue,wed,thu)"
                )
    }

    private fun isChillTime(): (LocalDateTime) -> Match = {
        (it.dayOfWeek in listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY))
            .ifFailed("Not a chill time")
    }

    private fun isScheduled(): (LocalDateTime) -> Match = {
        (it.dayOfWeek in listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY))
            .ifFailed("Not scheduled (mo,tue,thu,sat)")
    }

}