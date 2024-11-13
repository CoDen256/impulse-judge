package io.github.coden.impulse.judge

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

@RestController
class JudgeController(
    private val wellpass: Wellpass,
) {
    val rule: Rule<CheckIns> = WellpassRule()

    @GetMapping("/")
    fun index(): String {
        return "Hi."
    }
    @GetMapping("/check")
    fun check(@RequestParam(required = false, defaultValue = "false") hard: Boolean): Mono<ResponseEntity<Verdict>> {
        println(hard)
        return wellpass
            .checkins(LocalDate.now().minusMonths(4), LocalDate.now())
            .timeout(Duration.ofSeconds(60))
            .map {
                val match = rule.test(it)
                Verdict(!match.allowed, match.reason, it)
            }
            .map {
                if (it.guilty){
                    ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(it)
                }
                else ResponseEntity
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


    data class Verdict(
        val guilty: Boolean,
        val reason: String,
        val evidence: Any?,
    )
}