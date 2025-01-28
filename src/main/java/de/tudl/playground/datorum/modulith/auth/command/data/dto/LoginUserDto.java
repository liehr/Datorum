package de.tudl.playground.datorum.modulith.auth.command.data.dto;

public record LoginUserDto(String userId, String username, String role, boolean success) {}
