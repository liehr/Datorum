package de.tudl.playground.datorum.modulith.eventstore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import de.tudl.playground.datorum.modulith.shared.event.Event;
import de.tudl.playground.datorum.modulith.shared.exception.ErrorProcessingEventException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.context.ApplicationContext;

/**
 * The {@code DefaultEventProcessorService} is a concrete implementation of the {@link EventProcessorService}.
 * It serves as the central component for dynamically processing domain events in an event-driven architecture.
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Registers event types annotated with {@link Event} dynamically using Spring's {@link ApplicationContext}.</li>
 *     <li>Processes stored events retrieved from an {@link EventStore} by deserializing them and applying the logic provided by a consumer.</li>
 *     <li>Manages a registry of event types that maps event names to their corresponding Java classes.</li>
 * </ul>
 *
 * <h2>Key Features</h2>
 * <ul>
 *     <li>Automatic event registration: Scans the application context for beans annotated with {@code @Event} and registers them at runtime.</li>
 *     <li>Event deserialization: Uses Jackson's {@link ObjectMapper} to deserialize JSON-encoded event data into corresponding Java objects.</li>
 *     <li>Custom error handling: Wraps deserialization or processing errors in a {@link ErrorProcessingEventException} to provide consistent error reporting.</li>
 * </ul>
 *
 * <h2>Design Considerations</h2>
 * <ul>
 *     <li>Uses reflection to dynamically discover and register event classes annotated with {@link Event}.
 *         This ensures that new event types can be seamlessly added without manual registration.</li>
 *     <li>Maintains a registry of event types in {@code eventTypeRegistry} to map event names to classes for deserialization.</li>
 *     <li>Relies on {@link ObjectMapper} for deserialization, ensuring that events conform to expected JSON structures.</li>
 * </ul>
 *
 * <h2>Error Handling</h2>
 * <ul>
 *     <li>If an unknown event type is encountered, an {@link IllegalArgumentException} is thrown.</li>
 *     <li>If an error occurs during deserialization or processing, an {@link ErrorProcessingEventException} is raised.</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 * This class is thread-safe as the {@code eventTypeRegistry} is a synchronized collection, and the processing logic
 * operates on local data structures within method calls.
 *
 * @see EventProcessorService
 * @see EventStore
 * @see ObjectMapper
 * @see Event
 */
@Service
public class DefaultEventProcessorService implements EventProcessorService {

    private final ObjectMapper objectMapper;
    private final Map<String, Class<?>> eventTypeRegistry = new HashMap<>();

    public DefaultEventProcessorService(ObjectMapper objectMapper, ApplicationContext applicationContext) {
        this.objectMapper = objectMapper;

        Map<String, Object> eventBeans = applicationContext.getBeansWithAnnotation(Event.class);
        eventBeans.values().forEach(bean ->
                registerEventType(bean.getClass().getSimpleName(), bean.getClass())
        );
    }

    @SneakyThrows
    @Override
    public void processEvent(EventStore eventStore, Consumer<Object> eventConsumer) {
        try {
            String eventType = eventStore.getEventType();
            String eventData = eventStore.getEventData();

            Class<?> eventClass = eventTypeRegistry.get(eventType);
            if (eventClass == null) {
                throw new IllegalArgumentException("Unknown event type: " + eventType);
            }

            Object deserializedEvent = objectMapper.readValue(eventData, eventClass);
            eventConsumer.accept(deserializedEvent);

        } catch (Exception e) {
            throw new ErrorProcessingEventException(
                    "Error processing event: " + eventStore.getEventType(),
                    e
            );
        }
    }

    @Override
    public void registerEventType(String eventType, Class<?> eventClass) {
        eventTypeRegistry.put(eventType, eventClass);
    }
}


