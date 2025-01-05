package de.tudl.playground.datorum.modulith.eventstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudl.playground.datorum.modulith.eventstore.service.EventPublishingService;
import de.tudl.playground.datorum.modulith.eventstore.service.EventStoreService;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * Service responsible for publishing events and storing them in the event store.
 * <p>
 * This service combines event persistence with event publication, ensuring that events are
 * reliably stored and simultaneously made available for consumption by other components
 * in the application. It enables both event-driven architecture and event sourcing capabilities.
 * </p>
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Storing events in the event store for persistence and historical tracking.</li>
 *   <li>Publishing events to the Spring application context for event-driven communication.</li>
 * </ul>
 * </p>
 *
 * <h3>Usage Example</h3>
 * <pre>
 * {@code
 * EventPublisher eventPublisher = new EventPublisher(eventStoreService, eventPublishingService);
 *
 * MyEvent event = new MyEvent("example data");
 * eventPublisher.publishEvent(event);
 * }
 * </pre>
 *
 * <h3>Thread Safety</h3>
 * This service is thread-safe if the underlying {@link EventStoreService} and
 * {@link EventPublishingService} are thread-safe.
 *
 * @see EventStoreService
 * @see EventPublishingService
 */
@Service
public class EventPublisher {

    private final EventStoreService eventStoreService;
    private final EventPublishingService eventPublishingService;

    /**
     * Constructs an {@code EventPublisher} with the specified event store and publishing services.
     *
     * @param eventStoreService the service used to persist events in the event store.
     * @param eventPublishingService the service used to publish events to the application context.
     */
    public EventPublisher(
            EventStoreService eventStoreService,
            EventPublishingService eventPublishingService
    ) {
        this.eventStoreService = eventStoreService;
        this.eventPublishingService = eventPublishingService;
    }

    /**
     * Publishes an event by saving it in the event store and then publishing it to the application context.
     * <p>
     * This method extracts the aggregate ID from the provided event, persists the event in the
     * event store, and then publishes it to the Spring application context, enabling further
     * processing by event listeners.
     * </p>
     *
     * <h3>Example</h3>
     * <pre>
     * {@code
     * MyEvent event = new MyEvent("data");
     * eventPublisher.publishEvent(event);
     * }
     * </pre>
     *
     * @param event the event to be published. The event must have a method annotated with {@link AggregateId}
     *              to provide the aggregate ID.
     * @throws RuntimeException if there is an error extracting the aggregate ID or persisting the event.
     */
    @SneakyThrows
    public void publishEvent(Object event) {
        String aggregateId = extractAggregateId(event);

        // Store the event in the event store
        eventStoreService.saveEvent(aggregateId, event);

        // Publish the event to the application context
        eventPublishingService.publishEvent(event);
    }

    /**
     * Extracts the aggregate ID from the event by locating a method annotated with {@link AggregateId}.
     * <p>
     * This method reflects on the event class to find a method annotated with {@link AggregateId}
     * and invokes it to retrieve the aggregate ID.
     * </p>
     *
     * @param event the event from which to extract the aggregate ID.
     * @return the aggregate ID of the event as a {@link String}.
     * @throws RuntimeException if no method with the {@link AggregateId} annotation is found or if
     *                          invoking the method fails.
     */
    private String extractAggregateId(Object event) {
        Method aggregateIdMethod = findAggregateIdMethod(event.getClass());
        try {
            return (String) aggregateIdMethod.invoke(event);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error invoking @AggregateId method on event: " +
                            event.getClass().getName(),
                    e
            );
        }
    }

    /**
     * Finds a method in the event class that is annotated with {@link AggregateId}.
     * <p>
     * This method searches the declared methods of the event class for one that is
     * annotated with {@link AggregateId}. The method must return a {@link String}.
     * </p>
     *
     * @param eventClass the class of the event to search for the {@link AggregateId} method.
     * @return the {@link Method} annotated with {@link AggregateId}.
     * @throws IllegalArgumentException if no method with the {@link AggregateId} annotation is found.
     * @throws IllegalStateException if the method annotated with {@link AggregateId} does not return a {@link String}.
     */
    private Method findAggregateIdMethod(Class<?> eventClass) {
        for (Method method : eventClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AggregateId.class)) {
                if (method.getReturnType() != String.class) {
                    throw new IllegalStateException(
                            "@AggregateId method must return a String: " +
                                    method.getName() +
                                    " in class " +
                                    eventClass.getName()
                    );
                }
                return method;
            }
        }
        throw new IllegalArgumentException(
                "No @AggregateId method found in class: " + eventClass.getName()
        );
    }
}
