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
public class LoginController {

    private final StageSwitcher stageSwitcher;

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {

        // Navigate to the main stage
        stageSwitcher.switchTo("/fxml/main/Main.fxml");
    }

    @FXML
    public void goToRegister(ActionEvent event) {

        stageSwitcher.switchTo("/fxml/register/Register.fxml");
    }
}