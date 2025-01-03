package de.tudl.playground.datorum.ui;

import de.tudl.playground.datorum.ui.event.StageReadyEvent;
import de.tudl.playground.datorum.ui.service.AuthService;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("classpath:/fxml/login/Login.fxml")
    private Resource loginResource;

    @Value("classpath:/fxml/main/Main.fxml")
    private Resource mainResource;

    private final ApplicationContext applicationContext;

    private final String applicationTitle;

    private final AuthService authService;

    public StageInitializer(ApplicationContext applicationContext, @Value("${spring.application.ui.title}") String applicationTitle, AuthService authService) {
        this.applicationContext = applicationContext;
        this.applicationTitle = applicationTitle;
        this.authService = authService;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        StageSwitcher.setStage(stage);

        String token = authService.getToken().orElse(null);
        if (token == null || !authService.isTokenValid(token)) {
            loadStage(stage, loginResource);
        } else {
            loadStage(stage, mainResource);
        }
    }

    private void loadStage(Stage stage, Resource resource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(resource.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlLoader.load();
            stage.setScene(new Scene(parent, 800, 600));
            stage.setTitle(applicationTitle);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load FXML", e);
        }
    }
}
