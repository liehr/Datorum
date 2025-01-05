package de.tudl.playground.datorum.modulith.auth.command.aggregate;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudl.playground.datorum.modulith.auth.command.data.dto.LoginUserDto;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class AuthAggregate {

    private String aggregateId;

    private String lastLoginAttemptUsername;

    private boolean lastLoginSuccess;

    private final List<Object> changes = new ArrayList<>();

    public void handleLoginAttempt(LoginUserDto loginDto) {
        if (loginDto.success()) apply(
                new LoginSuccessfulEvent(
                        loginDto.username(),
                        lastLoginAttemptUsername,
                        lastLoginSuccess,
                        LocalDateTime.now().toString()
                )
        );
        else apply(
                new LoginFailedEvent(
                        loginDto.username(),
                        lastLoginAttemptUsername,
                        lastLoginSuccess,
                        LocalDateTime.now().toString()
                )
        );
    }

    private void apply(Object event) {
        this.changes.add(event);
        applyEvent(event);
    }

    private void applyEvent(Object event) {
        if (event instanceof LoginSuccessfulEvent loginSuccess) {
            this.aggregateId = loginSuccess.username();
            this.lastLoginAttemptUsername = loginSuccess.username();
            this.lastLoginSuccess = true;
        } else if (event instanceof LoginFailedEvent loginFailed) {
            this.aggregateId = loginFailed.username();
            this.lastLoginAttemptUsername = loginFailed.username();
            this.lastLoginSuccess = false;
        }
    }

    public void rehydrate(List<Object> events) {
        events
                .stream()
                .filter(event -> event instanceof List<?>)
                .map(event -> (List<?>) event)
                .flatMap(List::stream)
                .filter(EventStore.class::isInstance)
                .map(EventStore.class::cast)
                .forEach(this::processEvent);
    }

    private void processEvent(EventStore eventStore) {
        try {
            String eventType = eventStore.getEventType();
            String eventData = eventStore.getEventData();

            Object deserializedEvent = deserializeEvent(eventType, eventData);

            if (deserializedEvent != null) applyEvent(deserializedEvent);
            else throw new IllegalArgumentException(
                    "Unknow event type: " + eventType
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error processing event: " + eventStore.getEventType(),
                    e
            );
        }
    }

    private Object deserializeEvent(String eventType, String eventData)
            throws Exception {
        return switch (eventType) {
            case "LoginSuccessfulEvent" -> new ObjectMapper()
                    .readValue(eventData, LoginSuccessfulEvent.class);
            case "LoginFailedEvent" -> new ObjectMapper()
                    .readValue(eventData, LoginFailedEvent.class);
            default -> null;
        };
    }
}
