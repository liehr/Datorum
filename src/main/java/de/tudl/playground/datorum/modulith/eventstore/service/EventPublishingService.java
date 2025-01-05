package de.tudl.playground.datorum.modulith.eventstore.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Service responsible for publishing events to the Spring application context.
 * <p>
 * This service acts as a wrapper around Spring's {@link ApplicationEventPublisher},
 * simplifying the process of event publication within the application.
 * It enables event-driven communication by publishing events, which can then
 * be handled by any listeners registered within the Spring application context.
 * </p>
 * <p>
 * Typical use cases include:
 * <ul>
 *   <li>Notifying other components of domain events in a decoupled manner.</li>
 *   <li>Triggering asynchronous actions based on published events.</li>
 *   <li>Facilitating integration with external systems via event listeners.</li>
 * </ul>
 * </p>
 *
 * <h3>Example Usage</h3>
 * <pre>
 * {@code
 * @Autowired
 * private EventPublishingService eventPublishingService;
 *
 * public void someBusinessMethod() {
 *     MyEvent event = new MyEvent("example data");
 *     eventPublishingService.publishEvent(event);
 * }
 * }
 * </pre>
 *
 * <h3>Thread Safety</h3>
 * This service is thread-safe, as it delegates event publishing to the Spring
 * {@link ApplicationEventPublisher}, which is inherently thread-safe.
 *
 * @see ApplicationEventPublisher
 */
@Service
public class EventPublishingService {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs an {@code EventPublishingService} with the specified {@link ApplicationEventPublisher}.
     *
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} used to publish events
     *                                   within the Spring application context.
     */
    public EventPublishingService(
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publishes an event to the Spring application context.
     * <p>
     * The specified event will be passed to the Spring {@link ApplicationEventPublisher},
     * which will then notify all matching event listeners registered in the application context.
     * This method enables a decoupled communication pattern between components by relying on
     * the event-driven architecture.
     * </p>
     *
     * <h3>Example</h3>
     * <pre>
     * {@code
     * MyEvent event = new MyEvent("example data");
     * eventPublishingService.publishEvent(event);
     * }
     * </pre>
     *
     * @param event the event object to be published. This object can be of any type and will
     *              be delivered to all listeners that handle events of the same type or its
     *              subclasses.
     * @throws IllegalArgumentException if the provided event is {@code null}.
     * @see ApplicationEventPublisher#publishEvent(Object)
     */
    public void publishEvent(Object event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }
        applicationEventPublisher.publishEvent(event);
    }
}
