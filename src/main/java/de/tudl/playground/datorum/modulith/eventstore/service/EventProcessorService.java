package de.tudl.playground.datorum.modulith.eventstore.service;

import de.tudl.playground.datorum.modulith.eventstore.EventStore;

import java.util.function.Consumer;

public interface EventProcessorService {
    /**
     * Processes an event from the event store, deserializing and applying it to the aggregate.
     *
     * @param eventStore the event store containing event type and data.
     * @param eventConsumer the consumer that applies the event to the aggregate.
     */
    void processEvent(EventStore eventStore, Consumer<Object> eventConsumer);

    /**
     * Registers a new event type with its corresponding class for deserialization.
     *
     * @param eventType the event type as a string.
     * @param eventClass the class representing the event type.
     */
    void registerEventType(String eventType, Class<?> eventClass);
}

