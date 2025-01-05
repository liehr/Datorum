package de.tudl.playground.datorum.modulith.eventstore.exception;

public class ErrorInvokingAggregateException extends Exception {
    public ErrorInvokingAggregateException(String s, Exception e) {
        super(s, e);
    }
}
