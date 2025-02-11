package io.github.coden.impulse.judge.rules

import io.github.coden.impulse.judge.api.Match
import io.github.coden.impulse.judge.api.Rule
import io.github.coden.wellpass.api.CheckIns
import java.time.LocalDateTime

class WellpassRule : Rule<CheckIns> {
    override fun test(entity: CheckIns): Match {
        val checkIns = entity.checkIns
        val last = checkIns.maxByOrNull { it.checkInDate }
        return checkIns
            .filter { it.name.lowercase().contains("yoga|boulder|fitness first|kletter|fit/one".toRegex()) &&
            it.name.lowercase().contains("leipzig|plagwitz".toRegex())}
            .any { it.checkInDate.isAfter(LocalDateTime.now().minusDays(5)) }
            .asMatch("Last checkin was more than 5 days ago:  ${last?.name} on ${last?.checkInDate}")
    }
}