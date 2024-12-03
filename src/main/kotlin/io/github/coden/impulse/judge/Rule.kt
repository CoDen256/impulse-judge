package io.github.coden.impulse.judge

interface Rule<I> {
    fun test(entity: I): Match
}

data class Match(
    val allowed: Boolean,
    val reason: String,
) {


    infix  fun and(other: Match): Match {
        return Match(allowed && other.allowed, "("+reason+" && "+other.reason+")")
    }
    infix fun or(other: Match): Match {
        return Match(allowed || other.allowed, "("+reason+" || "+other.reason+")")
    }

    companion object {
        fun Boolean.ifFailed(failed: String, allowed: String = "ok"): Match {
            return Match(this, if (this) allowed else failed)
        }
    }
}