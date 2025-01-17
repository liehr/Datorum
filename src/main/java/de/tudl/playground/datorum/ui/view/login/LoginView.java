package de.tudl.playground.datorum.ui.view.login;

import de.tudl.playground.datorum.ui.controller.LoginController;
import de.tudl.playground.datorum.ui.view.ApplicationView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class LoginView implements ApplicationView {

    private final LoginController loginController;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
    }

    public Scene createScene() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPrefWidth(1280);
        root.setPrefHeight(720);

        Label headerLabel = new Label("Login");
        headerLabel.getStyleClass().add("header-label");

        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setSpacing(10);
        formContainer.getStyleClass().add("form-container");

        Label usernameLabel = new Label("Benutzername:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Gib deinen Benutzernamen ein");
        usernameField.setTooltip(new Tooltip("Bitte Benutzernamen angeben."));

        Label passwordLabel = new Label("Passwort:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Gib dein Passwort ein");
        passwordField.setTooltip(new Tooltip("Bitte Passwort angeben."));

        Button loginButton = new Button("Anmelden");
        loginButton.setOnAction(e -> loginController.handleLogin(usernameField.getText(), passwordField.getText()));

        Hyperlink registerLink = new Hyperlink("Noch keinen Account? Registrieren!");
        registerLink.setOnAction(e -> loginController.goToRegister());

        formContainer.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                loginButton, registerLink
        );

        root.getChildren().addAll(headerLabel, formContainer);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/Login.css").toExternalForm());

        return scene;
    }
}
