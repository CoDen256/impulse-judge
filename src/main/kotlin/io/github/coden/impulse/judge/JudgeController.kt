package io.github.coden.impulse.judge

import io.github.coden.absence.api.AbsenceService
import io.github.coden.absence.api.OutOfOffice
import io.github.coden.impulse.judge.api.Rule
import io.github.coden.impulse.judge.rules.AbsenceRule
import io.github.coden.impulse.judge.rules.HardCheckRule
import io.github.coden.impulse.judge.rules.TimeRule
import io.github.coden.wellpass.api.CheckIns
import io.github.coden.wellpass.api.Wellpass
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
class JudgeController(
    private val wellpass: Wellpass,
    private val absenceService: AbsenceService
) {
    val wellpassRule: Rule<CheckIns> = WellpassRule()
    val timeRule: Rule<LocalDateTime> = TimeRule()
    val absenceRule: Rule<List<OutOfOffice>> = AbsenceRule()
    val hardRule: Rule<Boolean> = HardCheckRule()

    @GetMapping("/")
    fun index(): String {
        return "Hi."
    }

    @GetMapping("/check")
    fun check(@RequestParam(required = false, defaultValue = "false") hard: Boolean): Mono<ResponseEntity<Verdict>> {
        return wellpass
            .checkins(LocalDate.now().minusMonths(4), LocalDate.now())
            .timeout(Duration.ofSeconds(60))
            .map {
                val absences = try {
                    absenceService.getAbsences()
                } catch (e: Exception) {
                    emptyList()
                }
                val match = (wellpassRule(it).and(timeRule(LocalDateTime.now())))
                    .or(absenceRule(absences))
                    .or(hardRule(hard))
                Verdict(
                    match.allowed,
                    match.reason,
                    it.copy(checkIns = it.checkIns.sortedByDescending { it.checkInDate })
                )
            }
            .map {
                if (!it.innocent) {
                    ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(it)
                } else ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(it)
            }
    }

    @GetMapping("/wellpass")
    fun wellpass(): Mono<CheckIns> {
        return wellpass
            .checkins(LocalDate.now())
            .timeout(Duration.ofSeconds(60))
    }

    @GetMapping("/absence")
    fun absence(): Mono<List<OutOfOffice>> {
        return Mono.fromSupplier { absenceService.getAbsences() }
    }

    data class Verdict(
        val innocent: Boolean,
        val reason: String,
        val evidence: Any?,
    )
}