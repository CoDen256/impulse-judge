package io.github.coden.impulse.judge.api

fun interface Rule<I>: (I) -> Match {
    override fun invoke(entity: I): Match = test(entity)

    fun test(entity: I): Match

    infix fun and(other: Rule<I>): Rule<I> {
        return Rule { this.test(it) and other.test(it) }
    }

    infix fun or(other: Rule<I>): Rule<I> {
        return Rule { this.test(it) or other.test(it) }
    }
}

data class Match(
    val allowed: Boolean,
    val reason: String,
) {
    infix fun and(other: Match): Match {
        return Match(allowed && other.allowed, "("+reason+" && "+other.reason+")")
    }
    infix fun or(other: Match): Match {
        return Match(allowed || other.allowed, "("+reason+" || "+other.reason+")")
    }

    companion object {
        fun Boolean.asMatch(ifFailed: String, ifAllowed: String = "ok"): Match {
            return Match(this, if (this) ifAllowed else ifFailed)
        }
    }
}