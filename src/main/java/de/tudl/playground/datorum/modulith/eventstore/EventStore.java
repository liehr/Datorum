package de.tudl.playground.datorum.modulith.eventstore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a stored event in the event store.
 * <p>
 * This class is used to persist events that have been generated in the system.
 * It contains information about the event's aggregate ID, event type, event data, and the timestamp when the event was created.
 * </p>
 */
@Entity
@Getter
@Setter
public class EventStore {

    /**
     * Unique identifier for the event stored in the database.
     * This ID is automatically generated when the event is saved in the event store.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The aggregate ID associated with the event.
     * This ID identifies the aggregate (such as a user or order) that the event pertains to.
     */
    private String aggregateId;

    /**
     * The type of the event, usually the class name or a custom event type string.
     * This is used to differentiate between different kinds of events in the system.
     */
    private String eventType;

    /**
     * The serialized data of the event in JSON format.
     * This field stores the event data as a string, which can be deserialized later for processing.
     */
    private String eventData;

    /**
     * The timestamp indicating when the event was created and stored.
     * This field is automatically set to the current time when the event is stored in the event store.
     */
    private LocalDateTime createdAt;
}
