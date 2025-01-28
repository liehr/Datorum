package de.tudl.playground.datorum.modulith.shared.token.data;

import java.util.List;
import java.util.UUID;

public record Token(UUID userId, String username, List<String> roles, String expirationDate, String signature) {}
