package de.tudl.playground.datorum.modulith.shared.token.data;

import java.util.List;

public record Token(String username, List<String> Roles, String expirationDate, String signature) {}
