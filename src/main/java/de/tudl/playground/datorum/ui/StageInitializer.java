package de.tudl.playground.datorum.ui;

import de.tudl.playground.datorum.commandgateway.CommandGateway;
import de.tudl.playground.datorum.modulith.auth.command.commands.CreateUserCommand;
import de.tudl.playground.datorum.ui.event.StageReadyEvent;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final CommandGateway commandGateway;

    public StageInitializer(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.setTitle("Datorum");
        stage.show();

        CreateUserCommand command = new CreateUserCommand(
                UUID.randomUUID().toString(),
                "Test",
                "Test",
                "Test",
                "User"
        );

        commandGateway.send(command);
    }
}
