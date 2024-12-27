package de.tudl.playground.datorum.modulith.eventstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.lang.reflect.Method;

/**
 * Service responsible for publishing events and storing them in the event store.
 * <p>
 * This service handles two key responsibilities:
 * 1. Storing the event in the event store with its aggregate ID, event type, event data, and timestamp.
 * 2. Publishing the event to the Spring application context to allow for further processing or event-driven actions.
 * </p>
 */
@Service
public class EventPublisher {

    private final EventStoreRepository eventStoreRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs an {@code EventPublisher} with the given repositories and event publisher.
     *
     * @param eventStoreRepository the repository used to persist events in the event store.
     * @param applicationEventPublisher the publisher used to publish events in the Spring application context.
     */
    public EventPublisher(EventStoreRepository eventStoreRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.eventStoreRepository = eventStoreRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publishes an event by saving it in the event store and then publishing it to the application context.
     * <p>
     * The method extracts the aggregate ID from the event, serializes the event data to JSON,
     * stores it in the event store, and finally publishes it to the Spring application context.
     * </p>
     *
     * @param event the event to be published.
     * @throws RuntimeException if there is an error extracting the aggregate ID or storing the event.
     */
    @SneakyThrows
    public void publishEvent(Object event) {
        String aggregateId = extractAggregateId(event);

        EventStore eventStore = new EventStore();
        eventStore.setAggregateId(aggregateId);
        eventStore.setEventType(event.getClass().getSimpleName());
        eventStore.setEventData(new ObjectMapper().writeValueAsString(event));
        eventStore.setCreatedAt(LocalDateTime.now());

        eventStoreRepository.save(eventStore);

        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Extracts the aggregate ID from the event by searching for a method annotated with {@link AggregateId}.
     *
     * @param event the event from which to extract the aggregate ID.
     * @return the aggregate ID of the event as a {@link String}.
     * @throws RuntimeException if no method with the {@link AggregateId} annotation is found or if the method invocation fails.
     */
    private String extractAggregateId(Object event) {
        Method aggregateIdMethod = findAggregateIdMethod(event.getClass());
        try {
            return (String) aggregateIdMethod.invoke(event);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking @AggregateId method on event: " + event.getClass().getName(), e);
        }
    }

    /**
     * Finds a method in the event class that is annotated with {@link AggregateId}.
     *
     * @param eventClass the class of the event to search for the {@link AggregateId} method.
     * @return the {@link Method} annotated with {@link AggregateId}.
     * @throws IllegalArgumentException if no method with the {@link AggregateId} annotation is found.
     * @throws IllegalStateException if the {@link AggregateId} method does not return a {@link String}.
     */
    private Method findAggregateIdMethod(Class<?> eventClass) {
        for (Method method : eventClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AggregateId.class)) {
                if (method.getReturnType() != String.class) {
                    throw new IllegalStateException(
                            "@AggregateId method must return a String: " + method.getName() + " in class " + eventClass.getName()
                    );
                }
                return method;
            }
        }
        throw new IllegalArgumentException("No @AggregateId method found in class: " + eventClass.getName());
    }
}
