package de.tudl.playground.datorum.gateway.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

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

    /**
     * Constructs a {@code DefaultQueryGateway} with the specified {@link ApplicationContext}.
     * This constructor logs all registered {@link QueryHandler} beans at startup.
     *
     * @param applicationContext the Spring application context containing the registered query handlers.
     */
    public DefaultQueryGateway(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        // Log all the registered handlers at startup
        applicationContext.getBeansOfType(QueryHandler.class).forEach((name, handler) ->
                log.info("Registered handler: {} -> {}", name, handler.getClass().getName())
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
     * @param <R> the type of the result returned by the query handler.
     * @return the result of the query handling, returned by the matched {@link QueryHandler}.
     * @throws IllegalArgumentException if no handler is found for the query type.
     */
    @Override
    public <R> R query(Object query) {
        // Get the query's class type
        Class<?> queryType = query.getClass();

        // Get all the registered QueryHandler beans from the application context
        Map<String, QueryHandler> handlers = applicationContext.getBeansOfType(QueryHandler.class);

        // Loop through each handler to find the one that matches the query type
        for (QueryHandler handler : handlers.values()) {
            // Get the actual generic type of the handler's QueryHandler interface
            ParameterizedType parameterizedType = (ParameterizedType) handler.getClass().getGenericInterfaces()[0];
            Class<?> handlerQueryType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

            // Check if the handler's query type matches the query passed in
            if (handlerQueryType.isAssignableFrom(queryType)) {
                // Safely handle the query using the matched handler
                return (R) handler.handle(query);
            }
        }

        // If no matching handler is found, throw an exception
        throw new IllegalArgumentException("No handler found for query type: " + queryType.getName());
    }
}
