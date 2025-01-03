package de.tudl.playground.datorum.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@RequiredArgsConstructor
public class LoginController {

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        // Login-Logik
        System.out.println("Login durchgeführt.");
    }

    @FXML
    public void goToRegister(ActionEvent event) {
        // Weiterleitung zur Registrierungsseite
        System.out.println("Zur Registrierungsseite wechseln.");
    }
}

