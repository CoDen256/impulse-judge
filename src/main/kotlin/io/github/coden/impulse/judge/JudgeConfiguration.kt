package io.github.coden.impulse.judge

import io.github.coden.absence.ICSAbsenceService
import io.github.coden.absence.api.AbsenceService
import io.github.coden.wellpass.api.config.WellpassConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration()
@Import(WellpassConfiguration::class)
class JudgeConfiguration {
    @Bean
    fun absenceService(@Value("\${absence.ics}") url: String): AbsenceService {
        return ICSAbsenceService(url)
    }
}