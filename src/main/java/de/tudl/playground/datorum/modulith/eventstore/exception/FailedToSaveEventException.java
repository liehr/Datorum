package de.tudl.playground.datorum.modulith.eventstore.exception;

public class FailedToSaveEventException extends Exception {
    public FailedToSaveEventException(String s, Exception e) {
        super(s ,e);
    }
}
