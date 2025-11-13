package hsebank;

import hsebank.client.ApplicationController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for HSE Bank system.
 * Spring Boot application that implements CommandLineRunner to start the application controller.
 */
@SpringBootApplication
public class HseBankApplication implements CommandLineRunner {
    private final ApplicationController controller;

    /**
     * Constructs the HseBankApplication with the specified controller.
     *
     * @param controller the application controller to handle user interactions
     */
    public HseBankApplication(final ApplicationController controller) {
        this.controller = controller;
    }

    /**
     * Main method that serves as the entry point for the Spring Boot application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(HseBankApplication.class, args);
    }

    /**
     * Executes after the application context is loaded, starting the main application loop.
     *
     * @param args command line arguments (not used in this implementation)
     */
    @Override
    public void run(final String... args) {
        controller.run();
    }
}