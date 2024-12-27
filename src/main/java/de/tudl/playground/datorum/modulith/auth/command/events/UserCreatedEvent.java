package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;
import lombok.Getter;

/**
 * Event representing the creation of a new user.
 * This event is used to store the state of a user when they are created in the system.
 * The event includes all necessary information about the user to persist their details.
 *
 * @param userId       the unique identifier of the user being created. This field is marked with {@link AggregateId}
 *                     to indicate it is the aggregate root identifier.
 * @param username     the username chosen by the user.
 * @param passwordHash the hashed password of the user for secure storage.
 * @param passwordSalt the cryptographic salt used in hashing the user's password.
 * @param role         the role assigned to the user, which defines their permissions and access within the system.
 */
public record UserCreatedEvent(
        @AggregateId String userId,
        @Getter String username,
        @Getter String passwordHash,
        @Getter String passwordSalt,
        @Getter String role) {
}
