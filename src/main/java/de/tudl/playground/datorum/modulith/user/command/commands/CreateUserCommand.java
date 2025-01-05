package de.tudl.playground.datorum.modulith.user.command.commands;

import lombok.*;
import org.springframework.context.ApplicationEvent;

/**
 * Represents a command to create a new user in the system.
 * This command encapsulates all necessary user details required for creation.
 * <p>
 * Instances of this class are immutable and can be constructed using the builder pattern provided by Lombok's {@link Builder} annotation.
 * </p>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class CreateUserCommand extends ApplicationEvent {

    /**
     * The unique identifier for the user.
     * This ID should be unique across the system to prevent conflicts.
     */
    private final String userId;

    /**
     * The username chosen by the user.
     * This is the name that the user will use to log in and be identified by within the system.
     */
    private final String username;

    /**
     * The hashed representation of the user's password.
     * Storing passwords as hashes enhances security by preventing plain-text password storage.
     */
    private final String passwordHash;

    /**
     * The cryptographic salt used in hashing the user's password.
     * Salts add a layer of security by ensuring that identical passwords have different hashes.
     */
    private final String passwordSalt;

    /**
     * The role assigned to the user.
     * This defines the user's permissions and access levels within the system.
     */
    private final String role;

    /**
     * Constructor to initialize the CreateUserCommand with all properties.
     *
     * @param userId      The unique identifier for the user.
     * @param username    The username chosen by the user.
     * @param passwordHash The hashed password of the user.
     * @param passwordSalt The cryptographic salt used for hashing the password.
     * @param role        The role assigned to the user.
     */
    public CreateUserCommand(
            String userId,
            String username,
            String passwordHash,
            String passwordSalt,
            String role
    ) {
        super(userId);
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.role = role;
    }
}
