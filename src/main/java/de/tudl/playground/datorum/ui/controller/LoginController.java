package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.modulith.auth.command.commands.LoginUserCommand;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import de.tudl.playground.datorum.ui.view.MainView;
import de.tudl.playground.datorum.ui.view.register.RegisterView;
import javafx.scene.control.Alert;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    private final StageSwitcher stageSwitcher;

    private final CommandGateway commandGateway;

    public LoginController(StageSwitcher stageSwitcher, CommandGateway commandGateway) {
        this.stageSwitcher = stageSwitcher;
        this.commandGateway = commandGateway;
    }

    public void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Fehler", "Benutzername und Passwort dürfen nicht leer sein.");
            return;
        }

        LoginUserCommand loginUserCommand = new LoginUserCommand(username, password);
        commandGateway.send(loginUserCommand);
    }

    @EventListener
    public void on (LoginSuccessfulEvent event) {
        showAlert("Erfolg!", "Erfolgreich angemeldet!");
        stageSwitcher.switchTo(MainView.class);
    }

    @EventListener
    public void on (LoginFailedEvent event) {
        showAlert("Fehler", "Fehlerhafte Eingabe!");
    }

    public void goToRegister() {
        stageSwitcher.switchTo(RegisterView.class);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
