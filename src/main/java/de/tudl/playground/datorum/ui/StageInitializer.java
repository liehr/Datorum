package de.tudl.playground.datorum.ui;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.gateway.query.QueryGateway;
import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.ui.controller.UserController;
import de.tudl.playground.datorum.ui.event.StageReadyEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final UserController userController;

    public StageInitializer(CommandGateway commandGateway, QueryGateway queryGateway, UserController userController) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.userController = userController;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.setTitle("Datorum");
        stage.show();

        List<User> users = userController.getAllUsers();

        for(User user : users)
        {
            log.info(String.valueOf(user));
        }

        userController.updateUser("351346c4-90eb-4292-aa2d-694b5f49feea");
    }
}
