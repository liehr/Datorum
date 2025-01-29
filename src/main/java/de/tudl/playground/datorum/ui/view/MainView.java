package de.tudl.playground.datorum.ui.view;

import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.ui.controller.MainController;
import de.tudl.playground.datorum.ui.view.dashboard.DashboardView;
import de.tudl.playground.datorum.ui.view.nav.BottomNavigationBar;
import de.tudl.playground.datorum.ui.view.tile.TileComponent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class MainView implements ApplicationView {

    private final MainController mainController;
    private final AuthTokenProvider tokenProvider;
    private final ApplicationContext applicationContext;
    private DashboardView dashboardView;

    public MainView(MainController mainController, AuthTokenProvider tokenProvider, ApplicationContext applicationContext) {
        this.mainController = mainController;
        this.tokenProvider = tokenProvider;
        this.applicationContext = applicationContext;
    }

    private Node getAdminContent() {
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add("main-pane");

        // Ensure it fills the entire scene
        stackPane.setPrefSize(1280, 720);  // Adjust to match your scene size
        stackPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        List<Button> buttons = getButtons();
        BottomNavigationBar bottomNavigationBar = new BottomNavigationBar(buttons);

        Label title = new Label("Datorum Admin View");

        dashboardView = applicationContext.getBean(DashboardView.class);
        dashboardView.initialize();

        stackPane.getChildren().addAll(dashboardView.createNode(), title, bottomNavigationBar);
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        return stackPane;
    }

    private List<Button> getButtons() {
        Button homeButton = new Button("🏠");
        homeButton.setOnAction(e -> mainController.handleLogout());

        Button textButton = new Button("T");
        textButton.setOnAction(e -> mainController.handleBudgetCreate());

        Button imageButton = new Button("🖼️");
        imageButton.setOnAction(e -> log.info("Image clicked"));

        Button clipboardButton = new Button("📋");
        clipboardButton.setOnAction(e -> log.info("Clipboard clicked"));

        Button gridButton = new Button("🗂️");
        gridButton.setOnAction(e -> log.info("Grid clicked"));

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

        dashboardView.setScene(scene);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/BottomNavigationBar.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/Tiles.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/Main.css")).toExternalForm());

        return scene;
    }
}

