package de.tudl.playground.datorum.modulith.shared.exception;

public class ErrorHashingPasswordException extends Exception {
    public ErrorHashingPasswordException(String s, Exception e) {
        super(s, e);
    }
}
