package de.tudl.playground.datorum.gateway.query;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link QueryGateway} interface.
 *
 * <p>This class acts as the central gateway for handling queries by delegating them to the appropriate {@link QueryHandler}s.
 * It uses the Spring {@link ApplicationContext} to discover all registered handlers and the {@link HandlerResolver}
 * to determine which handlers are applicable for a given query.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Logs all registered handlers during application startup for visibility and debugging.</li>
 *   <li>Supports resolving and invoking multiple handlers for a query when applicable.</li>
 *   <li>Provides appropriate logging and error handling when no handlers or multiple handlers are found.</li>
 * </ul>
 *
 * <p>Annotated with {@link Service} to make it available for dependency injection in a Spring context, and {@link Slf4j}
 * for logging capabilities.</p>
 *
 * @see QueryGateway
 * @see QueryHandler
 * @see HandlerResolver
 */
@Service
@Slf4j
public class DefaultQueryGateway implements QueryGateway {

    private final ApplicationContext applicationContext;
    private final HandlerResolver handlerResolver;

    /**
     * Constructs a new {@code DefaultQueryGateway}.
     *
     * @param applicationContext the Spring {@link ApplicationContext} to discover registered handlers.
     * @param handlerResolver the {@link HandlerResolver} to determine the appropriate handlers for a query.
     */
    public DefaultQueryGateway(ApplicationContext applicationContext, HandlerResolver handlerResolver) {
        this.applicationContext = applicationContext;
        this.handlerResolver = handlerResolver;

        // Log all the registered handlers at startup
        applicationContext
                .getBeansOfType(QueryHandler.class)
                .forEach((name, handler) ->
                        log.info(
                                "Registered handler: {} -> {}",
                                name,
                                handler.getClass().getName()
                        )
                );
    }

    /**
     * Processes a given query by delegating it to one or more matching {@link QueryHandler}s.
     *
     * <p>The method performs the following steps:</p>
     * <ol>
     *   <li>Determines the type of the query using {@link Object#getClass()}.</li>
     *   <li>Fetches all registered {@link QueryHandler}s from the Spring {@link ApplicationContext}.</li>
     *   <li>Uses the {@link HandlerResolver} to filter and prioritize handlers matching the query type.</li>
     *   <li>If no handlers are found, throws an {@link IllegalArgumentException}.</li>
     *   <li>If multiple handlers are found, logs a warning and executes all handlers, collecting their results.</li>
     *   <li>Returns the result from the appropriate handler(s) wrapped in an {@link Optional}.</li>
     * </ol>
     *
     * @param query the query object to process.
     * @param <Q> the type of the query.
     * @param <R> the type of the response expected from the handler(s).
     * @return an {@link Optional} containing the result, or {@link Optional#empty()} if no result is produced.
     * @throws IllegalArgumentException if no handlers are found for the query type.
     */
    @Override
    public <Q, R> Optional<R> query(Q query) {
        Class<?> queryType = query.getClass();

        // Fetch all registered handlers from the application context
        var allHandlers = applicationContext.getBeansOfType(QueryHandler.class).values();

        // Resolve matching handlers for the query type
        List<QueryHandler<Q, R>> matchingHandlers = handlerResolver.resolveHandlers(queryType, allHandlers);

        // Handle case where no handlers are found
        if (matchingHandlers.isEmpty()) {
            throw new IllegalArgumentException("No handler found for query type: " + queryType.getName());
        }

        // Handle a case where multiple handlers are found
        if (matchingHandlers.size() > 1) {
            log.warn("Multiple handlers found for query type: {}. Handlers: {}",
                    queryType.getName(),
                    matchingHandlers.stream()
                            .map(handler -> handler.getClass().getName())
                            .toList());

            // Execute all matching handlers and collect their results
            List<R> results = matchingHandlers.stream()
                    .map(handler -> handler.handle(query))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            // Return all results wrapped in an Optional
            return results.isEmpty() ? Optional.empty() : Optional.of((R) results);
        }

        // Single handler found, process the query
        return matchingHandlers.get(0).handle(query);
    }
}
