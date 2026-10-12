package com.austinlantz.papertrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Paper Trader application, a beginner-friendly
 * practice trading app that lets users buy stocks with no real money.
 *
 * @author Austin Lantz
 */
@SpringBootApplication
public class PapertraderApplication {

    /**
     * Starts the Spring Boot application and its embedded web server.
     *
     * @param args command-line arguments passed to Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(PapertraderApplication.class, args);
    }
}
