package de.tudl.playground.datorum.ui;

import de.tudl.playground.datorum.DatorumApplication;
import de.tudl.playground.datorum.ui.event.StageReadyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

public class FingoApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void init() {
        // Use the existing Spring context
        applicationContext = DatorumApplication.getApplicationContext();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}