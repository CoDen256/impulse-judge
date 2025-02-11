package io.github.coden.impulse.judge.rules

import io.github.coden.impulse.judge.api.Match
import io.github.coden.impulse.judge.api.Match.Companion.asMatch
import io.github.coden.impulse.judge.api.Rule

class HardCheckRule: Rule<Boolean> {
    override fun test(entity: Boolean): Match {
        return entity.asMatch("Not hard check")
    }
}