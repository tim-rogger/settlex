package ch.ledgerflow.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Einstiegspunkt des account-service.
 *
 * <p>{@code @SpringBootApplication} bündelt drei Annotationen:
 * <ul>
 *   <li>{@code @Configuration} – diese Klasse darf Beans definieren</li>
 *   <li>{@code @EnableAutoConfiguration} – Spring konfiguriert Tomcat, JSON usw. automatisch</li>
 *   <li>{@code @ComponentScan} – findet alle Beans in diesem Package und darunter</li>
 * </ul>
 */
@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
