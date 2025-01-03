package de.tudl.playground.datorum.ui.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private String jwtToken;

    public Optional<String> getToken() {
        return Optional.ofNullable(jwtToken);
    }

    public void saveToken(String token) {
        this.jwtToken = token;
    }

    public boolean isTokenValid(String token) {
        // Füge hier deine Logik zur Tokenvalidierung hinzu, z.B. Ablaufdatum überprüfen.
        return token != null && !token.isEmpty(); // Beispiel: einfache Prüfung
    }

    public void clearToken() {
        this.jwtToken = null;
    }
}

