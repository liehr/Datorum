package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.ui.util.StageSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterController {

    @FXML
    public TextField usernameField;

    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public PasswordField confirmPasswordField;

    private final StageSwitcher stageSwitcher;

    @FXML
    public void handleRegister(ActionEvent event) {



        goToLogin(event);
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        // Navigate back to login page
        stageSwitcher.switchTo("/fxml/login/Login.fxml");
    }
}
