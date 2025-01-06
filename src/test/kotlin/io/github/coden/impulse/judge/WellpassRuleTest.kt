package io.github.coden.impulse.judge

import io.github.coden.wellpass.api.CheckIn
import io.github.coden.wellpass.api.CheckIns
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class WellpassRuleTest {

    @Test
    fun test() {
        // setup
        assertEquals(Match(true, "ok"), WellpassRule().test(CheckIns(listOf(
            CheckIn("KOSMOS Boulderhalle Leipzig", LocalDateTime.now())))))
        assertEquals(Match(true, "ok"), WellpassRule().test(CheckIns(listOf(
            CheckIn("Ute Stephan Yoga Studio Plagwitz", LocalDateTime.now())))))
        assertEquals(Match(true, "ok"), WellpassRule().test(CheckIns(listOf(
            CheckIn("Onyx Boulderspot Leipzig", LocalDateTime.now())))))
        assertEquals(Match(true, "ok"), WellpassRule().test(CheckIns(listOf(
            CheckIn("Fitness First Leipzig - Messehof", LocalDateTime.now())))))
        assertEquals(Match(true, "ok"), WellpassRule().test(CheckIns(listOf(
            CheckIn("Kletterhalle NO LIMIT Leipzig", LocalDateTime.now())))))
        assertEquals(Match(true, "ok"), WellpassRule().test(CheckIns(listOf(
            CheckIn("FIT/ONE Leipzig", LocalDateTime.now())))))

        assertEquals(false, WellpassRule().test(CheckIns(listOf(
            CheckIn("Vinya Loft Yogastudio Bremen", LocalDateTime.now())))).allowed)
        assertEquals(false, WellpassRule().test(CheckIns(listOf(
            CheckIn("Fitness", LocalDateTime.now())))).allowed)

        // exercise

        // verify

    }
}