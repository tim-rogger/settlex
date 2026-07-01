package ch.settlex.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Rauch-Test: startet den kompletten Spring-Kontext.
 * Schlägt fehl, sobald die Verdrahtung (Beans, Config) kaputt ist –
 * ein einfacher, aber wirkungsvoller erster Test.
 */
@SpringBootTest
class AccountServiceApplicationTests {

    @Test
    void contextLoads() {
        // Wenn der Kontext startet, ist die Grundkonfiguration in Ordnung.
    }
}
