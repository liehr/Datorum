package de.tudl.playground.datorum;

import de.tudl.playground.datorum.ui.FingoApplication;
import javafx.application.Application;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class DatorumApplication {

    @Getter
    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        // Start the Spring context
        applicationContext = SpringApplication.run(DatorumApplication.class, args);
        // Launch the JavaFX application
        Application.launch(FingoApplication.class, args);
    }
}
