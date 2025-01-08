package de.tudl.playground.datorum.ui;

import de.tudl.playground.datorum.ui.controller.UserController;
import de.tudl.playground.datorum.ui.event.StageReadyEvent;
import de.tudl.playground.datorum.ui.exception.ErrorLoadingViewException;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import de.tudl.playground.datorum.ui.view.ApplicationView;
import de.tudl.playground.datorum.ui.view.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final ApplicationContext applicationContext;
    private final String applicationTitle;
    private final UserController userController;

    public StageInitializer(
            ApplicationContext applicationContext,
            @Value("${spring.application.ui.title}") String applicationTitle, UserController userController
    ) {
        this.applicationContext = applicationContext;
        this.applicationTitle = applicationTitle;
        this.userController = userController;
    }


    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        StageSwitcher.setStage(stage);

        userController.updateUser("72e564a6-fc3f-47ec-a076-4146125c0699");
        switchToView(stage);
    }

    @SneakyThrows
    private void switchToView(Stage stage) {
        try {
            ApplicationView view = applicationContext.getBean((Class<? extends ApplicationView>) LoginView.class);
            Scene scene = view.createScene();
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.show();
        } catch (Exception e) {
            throw new ErrorLoadingViewException("Cannot load view: " + LoginView.class.getName(), e);
        }
    }
}
