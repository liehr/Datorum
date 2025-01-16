package de.tudl.playground.datorum.modulith.user.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;
import de.tudl.playground.datorum.modulith.shared.event.Event;
import lombok.Getter;

/**
 * Event representing the update of an existing user's details.
 * This event is used to capture the changes made to a user's information in the system.
 * It includes the updated user data that needs to be persisted.
 *
 * @param userId       the unique identifier of the user being updated. This field is marked with {@link AggregateId}
 *                     to indicate it is the aggregate root identifier.
 * @param userName     the updated username of the user.
 * @param passwordHash the updated hashed password of the user for secure storage.
 * @param passwordSalt the updated cryptographic salt used in hashing the user's password.
 * @param role         the updated role assigned to the user, which defines their permissions and access within the system.
 */
@Event
public record UserUpdatedEvent(
        @AggregateId String userId,
        @Getter String userName,
        @Getter String passwordHash,
        @Getter String passwordSalt,
        @Getter String role
) {}
