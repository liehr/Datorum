package de.tudl.playground.datorum.modulith.auth.command.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a user in the system.
 * This entity class maps to the 'users' table in the database and encapsulates all necessary user details.
 * <p>
 * The {@code User} class utilizes JPA annotations to define the entity and its mappings to the database.
 * The {@code @Setter} annotation from Lombok generates setter methods for all fields, facilitating object construction and modification.
 * </p>
 */
@Entity
@Setter
public class User {

    /**
     * The unique identifier for the user.
     * This ID is automatically generated as a UUID upon entity creation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The username chosen by the user.
     * This is the name that the user will use to log in and be identified by within the system.
     */
    private String username;

    /**
     * The hashed representation of the user's password.
     * Storing passwords as hashes enhances security by preventing plain-text password storage.
     */
    private String passwordHash;

    /**
     * The cryptographic salt used in hashing the user's password.
     * Salts add an additional layer of security by ensuring that identical passwords have different hashes.
     */
    private String passwordSalt;

    /**
     * The role assigned to the user.
     * This defines the user's permissions and access levels within the system.
     */
    private String role;
}
