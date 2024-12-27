package de.tudl.playground.datorum.modulith.user.command.data.dto;

/**
 * A Data Transfer Object (DTO) for updating an existing user's information.
 * This record encapsulates the necessary details required to update a user's profile in the system.
 *
 * @param userName     the updated username of the user.
 * @param passwordHash the updated hashed representation of the user's password.
 * @param passwordSalt the updated cryptographic salt used in hashing the user's password.
 * @param role         the updated role assigned to the user, defining their permissions and access levels.
 */
public record UpdateUserDto(
        String userName,
        String passwordHash,
        String passwordSalt,
        String role) {
}
