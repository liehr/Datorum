package de.tudl.playground.datorum.ui.view;

import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.ui.controller.MainController;
import de.tudl.playground.datorum.ui.view.nav.BottomNavigationBar;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class MainView implements ApplicationView {

    private final MainController mainController;
    private final AuthTokenProvider tokenProvider;

    public MainView(MainController mainController, AuthTokenProvider tokenProvider) {
        this.mainController = mainController;
        this.tokenProvider = tokenProvider;
    }

    private Node getAdminContent() {
        StackPane stackPane = new StackPane();

        List<Button> buttons = getButtons();
        // Create BottomNavigationBar
        BottomNavigationBar bottomNavigationBar = new BottomNavigationBar(buttons);

        // Create Title Label
        Label title = new Label("Datorum Admin View");

        // Add children to the StackPane
        stackPane.getChildren().addAll(bottomNavigationBar, title);

        // Align components
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        return stackPane;
    }

    private List<Button> getButtons() {
        Button homeButton = new Button("🏠");
        homeButton.setOnAction(e -> mainController.handleLogout());

        Button textButton = new Button("T");
        textButton.setOnAction(e -> mainController.handleBudgetCreation());

        Button imageButton = new Button("🖼️");
        imageButton.setOnAction(e -> System.out.println("Image clicked"));

        Button clipboardButton = new Button("📋");
        clipboardButton.setOnAction(e -> System.out.println("Clipboard clicked"));

        Button gridButton = new Button("🗂️");
        gridButton.setOnAction(e -> System.out.println("Grid clicked"));

        return List.of(homeButton, textButton, imageButton, clipboardButton, gridButton);
    }

    @Override
    public Scene createScene() {
        AuthorizeView authorizeView = new AuthorizeView(tokenProvider);
        authorizeView.setPrefHeight(720.0);
        authorizeView.setPrefWidth(1280.0);
        authorizeView.setAuthorizedContent("ADMIN, USER", this::getAdminContent);

        authorizeView.setNotAuthorizedContent(
                () -> new Label("You are not authorized!"));

        Scene scene = new Scene(authorizeView);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/BottomNavigationBar.css")).toExternalForm());

        return scene;
    }
}

