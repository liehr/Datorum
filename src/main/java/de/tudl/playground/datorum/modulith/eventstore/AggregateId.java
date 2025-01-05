package de.tudl.playground.datorum.modulith.eventstore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark the method that returns the aggregate ID for an aggregate root.
 * <p>
 * This annotation is typically used to identify the method in an aggregate that is responsible
 * for providing the unique identifier (ID) for the aggregate. It ensures that the proper field or method
 * is recognized as the aggregate ID when handling events or other operations in the domain model.
 * </p>
 *
 * @see AggregateId
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AggregateId {
}
