package de.tudl.playground.datorum.ui.util;

import de.tudl.playground.datorum.ui.view.ApplicationView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StageSwitcher {

    private static Stage stage;
    private final ApplicationContext applicationContext;

    public static void setStage(Stage stage) {
        StageSwitcher.stage = stage;
    }

    public void switchTo(Class<? extends ApplicationView> viewClass) {
        try {
            ApplicationView view = applicationContext.getBean(viewClass);
            Scene scene = view.createScene();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Error switching to view: " + viewClass.getName(), e);
        }
    }
}
