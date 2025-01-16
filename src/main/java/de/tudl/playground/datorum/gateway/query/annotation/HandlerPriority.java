package de.tudl.playground.datorum.gateway.query.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerPriority {
    int value();
}
