package de.tudl.playground.datorum.ui.util;

import de.tudl.playground.datorum.ui.exception.ErrorLoadingViewException;
import de.tudl.playground.datorum.ui.view.ModalView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModalPresenter {

    private final ApplicationContext applicationContext;

    @SneakyThrows
    public void showModal(Class<? extends ModalView> viewClass) {
        try {
            // Get the modal view bean from the application context
            ModalView view = applicationContext.getBean(viewClass);

            // Create a new stage for the modal
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle(view.getTitle());

            // Create the scene from the modal view and set it on the stage
            Scene scene = view.createScene();
            modalStage.setScene(scene);

            // Show the modal and wait for it to close
            modalStage.showAndWait();
        } catch (Exception e) {
            throw new ErrorLoadingViewException("Error showing modal: " + viewClass.getName(), e);
        }
    }
}