package de.tudl.playground.datorum.modulith.user.command.data.dto;

/**
 * A Data Transfer Object (DTO) for creating a new user.
 * This record encapsulates all necessary information required to create a user in the system.
 *
 * @param userId       the unique identifier for the user.
 * @param userName     the username chosen by the user.
 * @param passwordHash the hashed representation of the user's password.
 * @param passwordSalt the cryptographic salt used in hashing the user's password.
 * @param role         the role assigned to the user, defining their permissions and access levels.
 */
public record CreateUserDto(
        String userId,
        String userName,
        String passwordHash,
        String passwordSalt,
        String role
) {}
