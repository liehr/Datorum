package de.tudl.playground.datorum.ui;

import de.tudl.playground.datorum.modulith.shared.token.TokenValidationService;
import de.tudl.playground.datorum.ui.event.StageReadyEvent;
import de.tudl.playground.datorum.ui.exception.ErrorLoadingViewException;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import de.tudl.playground.datorum.ui.view.ApplicationView;
import de.tudl.playground.datorum.ui.view.login.LoginView;
import de.tudl.playground.datorum.ui.view.MainView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final ApplicationContext applicationContext;
    private final String applicationTitle;
    private final TokenValidationService tokenValidationService;

    public StageInitializer(
            ApplicationContext applicationContext,
            @Value("${spring.application.ui.title}") String applicationTitle,
            TokenValidationService tokenValidationService
    ) {
        this.applicationContext = applicationContext;
        this.applicationTitle = applicationTitle;
        this.tokenValidationService = tokenValidationService;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        StageSwitcher.setStage(stage);
        switchToView(stage);
    }

    @SneakyThrows
    private void switchToView(Stage stage) {
        if (tokenValidationService.isValidToken()) {
            loadView(stage, MainView.class);
        } else {
            loadView(stage, LoginView.class);
        }
    }

    @SneakyThrows
    private void loadView(Stage stage, Class<? extends ApplicationView> viewClass) {
        try {
            ApplicationView view = applicationContext.getBean(viewClass);
            Scene scene = view.createScene();
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.show();
        } catch (Exception e) {
            throw new ErrorLoadingViewException("Cannot load view: " + viewClass.getName(), e);
        }
    }
}

