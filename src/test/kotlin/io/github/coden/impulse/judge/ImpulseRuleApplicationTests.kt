package io.github.coden.impulse.judge

import io.github.coden.wellpass.api.Wellpass
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import kotlin.test.assertTrue

@SpringBootTest
@Disabled
class ImpulseRuleApplicationTests {

    @Autowired
    lateinit var api: Wellpass

    @Test
    fun contextLoads() {
        val block = api.checkins(LocalDate.now()).block()
        assertTrue(!block?.checkIns.isNullOrEmpty())
    }

}
