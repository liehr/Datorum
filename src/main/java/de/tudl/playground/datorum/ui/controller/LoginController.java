package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.modulith.auth.command.commands.LoginUserCommand;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginController {

    private final StageSwitcher stageSwitcher;

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    private CommandGateway commandGateway;

    @Autowired
    public LoginController(
            StageSwitcher stageSwitcher,
            CommandGateway commandGateway
    ) {
        this.stageSwitcher = stageSwitcher;
        this.commandGateway = commandGateway;
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        LoginUserCommand command = new LoginUserCommand(
                usernameField.getText(),
                passwordField.getText()
        );

        commandGateway.send(command);
    }

    @FXML
    public void goToRegister(ActionEvent event) {
        stageSwitcher.switchTo("/fxml/register/Register.fxml");
    }
}
