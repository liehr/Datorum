package de.tudl.playground.datorum.modulith.shared.exception;

public class ErrorProcessingEventException extends Exception {
    public ErrorProcessingEventException(String s, Exception e) {
        super(s, e);
    }
}
