package de.tudl.playground.datorum.ui.util;

import java.io.IOException;

import de.tudl.playground.datorum.ui.exception.ResourceNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StageSwitcher {

    private static Stage stage; // Still static to allow global stage reference
    private final ApplicationContext applicationContext;

    public static void setStage(Stage stage) {
        StageSwitcher.stage = stage;
    }

    @SneakyThrows
    public void switchTo(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlLoader.load();
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            throw new ResourceNotFoundException("Error loading FXML: " + fxmlPath, e);
        }
    }
}
