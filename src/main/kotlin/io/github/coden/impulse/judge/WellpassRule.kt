package io.github.coden.impulse.judge

import io.github.coden.impulse.judge.Match.Companion.ifFailed
import io.github.coden.wellpass.api.CheckIns
import java.time.LocalDateTime

class WellpassRule : Rule<CheckIns> {
    override fun test(entity: CheckIns): Match {
        val checkIns = entity.checkIns
        val last = checkIns.maxByOrNull { it.checkInDate }
        return checkIns
            .filter { it.name.lowercase().contains("yoga|boulder|fitness first|kletter|fit/one".toRegex()) }
            .any { it.checkInDate.isAfter(LocalDateTime.now().minusDays(7)) }
            .ifFailed("Last checkin was more than 7 days ago:  ${last?.name} on ${last?.checkInDate}")
    }
}