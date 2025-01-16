package de.tudl.playground.datorum.ui.view;

import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.ui.controller.MainController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MainView implements ApplicationView{

    private final MainController mainController;
    private final AuthTokenProvider tokenProvider;

    public MainView(MainController mainController, AuthTokenProvider tokenProvider) {
        this.mainController = mainController;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Scene createScene() {
        AuthorizeView authorizeView = new AuthorizeView(tokenProvider);
        authorizeView.setPrefHeight(720.0);
        authorizeView.setPrefWidth(1280.0);
        authorizeView.setRoles("ADMIN", "USER");
        authorizeView.setAuthorizedContent(() -> {
            StackPane stackPane = new StackPane();
            stackPane.setPrefHeight(720.0);
            stackPane.setPrefWidth(1280.0);

            // Create BottomNavigationBar
            BottomNavigationBar bottomNavigationBar = new BottomNavigationBar();

            // Create Title Label
            Label title = new Label("Datorum");

            // Add children to the StackPane
            stackPane.getChildren().addAll(bottomNavigationBar, title);

            // Align components
            StackPane.setAlignment(bottomNavigationBar, Pos.BOTTOM_CENTER);
            StackPane.setAlignment(title, Pos.TOP_CENTER);

            return stackPane;
        });

        authorizeView.setNotAuthorizedContent(
                () -> new Label("You are not authorized!"));

        Scene scene = new Scene(authorizeView);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/BottomNavigationBar.css")).toExternalForm());

        return scene;
    }
}
