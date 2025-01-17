package de.tudl.playground.datorum.ui.view.register;

import de.tudl.playground.datorum.ui.view.ApplicationView;
import javafx.scene.Scene;
import de.tudl.playground.datorum.ui.controller.RegisterController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class RegisterView implements ApplicationView {

    private final RegisterController registerController;

    public RegisterView(RegisterController registerController) {
        this.registerController = registerController;
    }

    public Scene createScene() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPrefWidth(1280);
        root.setPrefHeight(720);

        Label headerLabel = new Label("Register");
        headerLabel.getStyleClass().add("header-label");

        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setSpacing(10);
        formContainer.getStyleClass().add("form-container");

        Label usernameLabel = new Label("Benutzername:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Wähle deinen Benutzernamen");
        usernameField.setTooltip(new Tooltip("Geben Sie einen eindeutigen Benutzernamen ein."));

        Label passwordLabel = new Label("Passwort:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Gib dein Passwort ein");
        passwordField.setTooltip(new Tooltip("Wähle ein sicheres Passwort."));

        Label confirmPasswordLabel = new Label("Passwort bestätigen:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Bestätige dein Passwort");
        confirmPasswordField.setTooltip(new Tooltip("Gebe dein Passwort erneut ein."));

        Button registerButton = new Button("Registrieren");
        registerButton.setOnAction(e -> registerController.handleRegister(
                usernameField.getText(),
                passwordField.getText(),
                confirmPasswordField.getText()
        ));

        Hyperlink loginLink = new Hyperlink("Hast du schon ein Konto? Dann melde dich an!");
        loginLink.setOnAction(e -> registerController.goToLogin());

        formContainer.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                confirmPasswordLabel, confirmPasswordField,
                registerButton, loginLink
        );

        root.getChildren().addAll(headerLabel, formContainer);

        Scene scene = new Scene(root);

        // CSS hinzufügen
        scene.getStylesheets().add(getClass().getResource("/styles/Register.css").toExternalForm());

        return scene;
    }
}

