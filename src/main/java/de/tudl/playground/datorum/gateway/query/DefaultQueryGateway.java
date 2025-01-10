package de.tudl.playground.datorum.gateway.query;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * The {@code DefaultQueryGateway} is the implementation of the {@link QueryGateway} interface that
 * handles executing queries by finding the appropriate {@link QueryHandler} for the given query type.
 * <p>
 * This gateway interacts with the Spring application context to locate and invoke the correct handler
 * based on the query's type. It allows for dynamic query handling, where handlers for different query types
 * are registered and invoked at runtime.
 * </p>
 * <p>
 * It logs all registered query handlers during the application startup for visibility.
 * </p>
 */
@Service
@Slf4j
public class DefaultQueryGateway implements QueryGateway {

    /**
     * The Spring application context, used to retrieve beans of type {@link QueryHandler}.
     */
    private final ApplicationContext applicationContext;
    private final HandlerResolver handlerResolver;

    /**
     * Constructs a {@code DefaultQueryGateway} with the specified {@link ApplicationContext}.
     * This constructor logs all registered {@link QueryHandler} beans at startup.
     *
     * @param applicationContext the Spring application context containing the registered query handlers.
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
     * Executes the given query by finding the appropriate {@link QueryHandler} based on the query's type.
     * <p>
     * This method dynamically locates a handler from the application context that matches the query type
     * and delegates the query handling to that handler. If no matching handler is found, an exception is thrown.
     * </p>
     *
     * @param query the query object that needs to be handled.
     * @param <R>   the type of the result returned by the query handler.
     * @return the result of the query handling, returned by the matched {@link QueryHandler}.
     * @throws IllegalArgumentException if no handler is found for the query type.
     */
    @Override
    public <Q, R> Optional<R> query(Q query) {
        Class<?> queryType = query.getClass();

        // Beans abrufen
        var allHandlers = applicationContext.getBeansOfType(QueryHandler.class).values();

        // Resolver für die passenden Handler aufrufen
        List<QueryHandler<Q, R>> matchingHandlers = handlerResolver.resolveHandlers(queryType, allHandlers);

        if (matchingHandlers.isEmpty()) {
            throw new IllegalArgumentException("No handler found for query type: " + queryType.getName());
        }

        if (matchingHandlers.size() > 1) {
            log.warn("Multiple handlers found for query type: {}. Handlers: {}",
                    queryType.getName(),
                    matchingHandlers.stream()
                            .map(handler -> handler.getClass().getName())
                            .toList());

            List<R> results = matchingHandlers.stream()
                    .map(handler -> handler.handle(query))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return results.isEmpty() ? Optional.empty() : Optional.of((R) results);
        }

        // Nur ein Handler vorhanden
        return matchingHandlers.get(0).handle(query);
    }
}
