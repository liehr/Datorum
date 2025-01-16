package de.tudl.playground.datorum.modulith.shared.event;

import de.tudl.playground.datorum.modulith.eventstore.service.EventProcessorService;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * The {@code EventRegistrationService} is responsible for discovering and registering event classes
 * annotated with the {@link Event} annotation. It dynamically scans the classpath for annotated event
 * types within a specified base package and registers them with the {@link EventProcessorService}.
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Scans the application classpath for event classes annotated with {@link Event}.</li>
 *     <li>Registers discovered event types with the {@link EventProcessorService} for further processing.</li>
 * </ul>
 *
 * <h2>Key Features</h2>
 * <ul>
 *     <li>Dynamic event discovery: Uses the {@code Reflections} library to perform runtime scanning for annotated classes.</li>
 *     <li>Automatic registration: Eliminates the need to manually register each event type, enhancing scalability.</li>
 *     <li>Integration with {@link EventProcessorService}: Automatically registers discovered event types for use in the event-driven system.</li>
 * </ul>
 *
 * <h2>Design Considerations</h2>
 * <ul>
 *     <li>Relies on the {@code Reflections} library for efficient runtime scanning of classes.</li>
 *     <li>Uses {@link EventProcessorService} to centralize event type registration.</li>
 *     <li>Supports modular architectures by allowing a configurable base package for scanning.</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 * This class is thread-safe as it performs only local operations and relies on thread-safe components like {@code Reflections}.
 *
 * <h2>Error Handling</h2>
 * <ul>
 *     <li>If no classes are found with the {@link Event} annotation, no action is taken.</li>
 *     <li>Assumes that all annotated classes are valid event types. Invalid classes may result in runtime exceptions
 *         during registration or processing.</li>
 * </ul>
 *
 * @author
 * @version 1.0
 * @see EventProcessorService
 * @see Event
 * @see Reflections
 */
@Service
public class EventRegistrationService {

    private final EventProcessorService eventProcessorService;

    /**
     * Constructs an {@code EventRegistrationService} with the specified {@link EventProcessorService}.
     *
     * @param eventProcessorService the service used to register discovered event types.
     */
    public EventRegistrationService(EventProcessorService eventProcessorService) {
        this.eventProcessorService = eventProcessorService;
    }

    /**
     * Scans the specified base package for classes annotated with {@link Event} and registers them with the
     * {@link EventProcessorService}.
     *
     * @param basePackage the base package to scan for annotated event classes.
     */
    public void registerAnnotatedEvent(String basePackage) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(basePackage)
                        .addScanners(Scanners.TypesAnnotated)
        );

        Set<Class<?>> eventClasses = reflections.getTypesAnnotatedWith(Event.class);

        for (Class<?> eventClass : eventClasses) {
            eventProcessorService.registerEventType(eventClass.getSimpleName(), eventClass);
        }
    }
}

