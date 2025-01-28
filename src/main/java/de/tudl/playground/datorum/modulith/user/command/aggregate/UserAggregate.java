package de.tudl.playground.datorum.modulith.user.command.aggregate;

import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import de.tudl.playground.datorum.modulith.user.command.data.dto.CreateUserDto;
import de.tudl.playground.datorum.modulith.user.command.data.dto.UpdateUserDto;
import de.tudl.playground.datorum.modulith.user.command.events.UserCreatedEvent;
import de.tudl.playground.datorum.modulith.user.command.events.UserUpdatedEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

import de.tudl.playground.datorum.modulith.eventstore.service.EventProcessorService;

/**
 * The {@code UserAggregate} class serves as the aggregate root for the user domain.
 * It encapsulates all the state and behaviors associated with a user entity.
 *
 * <p>This class delegates event deserialization to {@link EventProcessorService} to
 * simplify event processing and improve maintainability.</p>
 */
public class UserAggregate {

    private final EventProcessorService eventProcessorService;

    private String userId;

    @Getter
    private String userName;

    @Getter
    private String passwordHash;

    @Getter
    private String passwordSalt;

    @Getter
    private String role;

    @Getter
    private final List<Object> changes = new ArrayList<>();

    public UserAggregate(EventProcessorService eventProcessorService) {
        this.eventProcessorService = eventProcessorService;
    }

    public void createUser(CreateUserDto createDto) {
        if (this.userId != null) {
            throw new IllegalArgumentException("User already exists!");
        }

        apply(new UserCreatedEvent(
                createDto.userId(),
                createDto.userName(),
                createDto.passwordHash(),
                createDto.passwordSalt(),
                createDto.role()
        ));
    }

    public void updateUser(UpdateUserDto updateDto) {
        if (this.userId == null) {
            throw new IllegalArgumentException("User does not exist!");
        }

        apply(new UserUpdatedEvent(
                this.userId,
                updateDto.userName(),
                updateDto.passwordHash(),
                updateDto.passwordSalt(),
                updateDto.role()
        ));
    }

    private void apply(Object event) {
        this.changes.add(event);
        applyEvent(event);
    }

    private void applyEvent(Object event) {
        if (event instanceof UserCreatedEvent userCreatedEvent) {
            this.userId = userCreatedEvent.userId();
            this.userName = userCreatedEvent.username();
            this.passwordHash = userCreatedEvent.passwordHash();
            this.passwordSalt = userCreatedEvent.passwordSalt();
            this.role = userCreatedEvent.role();
        } else if (event instanceof UserUpdatedEvent userUpdatedEvent) {
            this.userName = userUpdatedEvent.userName();
            this.passwordHash = userUpdatedEvent.passwordHash();
            this.passwordSalt = userUpdatedEvent.passwordSalt();
            this.role = userUpdatedEvent.role();
        }
    }

    public void rehydrate(List<EventStore> events) {
        events.forEach(eventStore -> eventProcessorService.processEvent(eventStore, this::applyEvent));
    }
}

