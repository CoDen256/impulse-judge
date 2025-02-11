package io.github.coden.impulse.judge.rules

import io.github.coden.absence.api.OutOfOffice
import io.github.coden.impulse.judge.api.Match
import io.github.coden.impulse.judge.api.Match.Companion.asMatch
import io.github.coden.impulse.judge.api.Rule
import java.time.LocalDateTime

class AbsenceRule: Rule<List<OutOfOffice>> {
    override fun test(entity: List<OutOfOffice>): Match {
        val now = LocalDateTime.now()
        val latestAbsence: LocalDateTime = entity
            .maxOfOrNull { it.end }
            ?.plusDays(1)
            ?.atStartOfDay()
            ?: LocalDateTime.MIN

        return now
            .isBefore(latestAbsence.plusDays(5))
            .asMatch("Is not sick")
    }
}