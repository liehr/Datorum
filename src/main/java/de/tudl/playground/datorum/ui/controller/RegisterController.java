package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.modulith.shared.util.HashingUtil;
import de.tudl.playground.datorum.modulith.user.command.commands.CreateUserCommand;
import de.tudl.playground.datorum.modulith.user.command.events.UserCreatedEvent;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import de.tudl.playground.datorum.ui.view.LoginView;
import javafx.scene.control.Alert;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisterController {

    private final StageSwitcher stageSwitcher;

    private final CommandGateway commandGateway;

    public RegisterController(StageSwitcher stageSwitcher, CommandGateway commandGateway) {
        this.stageSwitcher = stageSwitcher;
        this.commandGateway = commandGateway;
    }

    public void handleRegister(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Fehler", "Alle Felder müssen ausgefüllt werden.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Fehler", "Die Passwörter stimmen nicht überein.");
            return;
        }

        String salt = UUID.randomUUID().toString() + UUID.randomUUID();

        CreateUserCommand createUserCommand = new CreateUserCommand(
                UUID.randomUUID().toString(),
                username,
                HashingUtil.hashPassword(password, salt),
                salt,
                "USER"
        );

        commandGateway.send(createUserCommand);
    }

    @EventListener
    private void on (UserCreatedEvent event) {
        showAlert("Erfolg", "Erfolgreich registriert!");
        stageSwitcher.switchTo(LoginView.class);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToLogin() {
        stageSwitcher.switchTo(LoginView.class);
    }
}
