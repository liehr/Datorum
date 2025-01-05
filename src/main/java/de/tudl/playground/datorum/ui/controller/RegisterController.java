package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.modulith.shared.util.HashingUtil;
import de.tudl.playground.datorum.modulith.user.command.commands.CreateUserCommand;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterController {

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public PasswordField confirmPasswordField;

    private final StageSwitcher stageSwitcher;

    private CommandGateway commandGateway;

    @Autowired
    public RegisterController(
            StageSwitcher stageSwitcher,
            CommandGateway commandGateway
    ) {
        this.stageSwitcher = stageSwitcher;
        this.commandGateway = commandGateway;
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String salt = UUID.randomUUID().toString() + UUID.randomUUID();

        CreateUserCommand createUserCommand = new CreateUserCommand(
                UUID.randomUUID().toString(),
                usernameField.getText(),
                HashingUtil.hashPassword(passwordField.getText(), salt),
                salt,
                "USER"
        );

        commandGateway.send(createUserCommand);

        goToLogin(event);
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        // Navigate back to login page
        stageSwitcher.switchTo("/fxml/login/Login.fxml");
    }
}
