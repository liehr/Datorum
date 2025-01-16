package de.tudl.playground.datorum.gateway.query.resolver;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.gateway.query.annotation.HandlerPriority;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link HandlerResolver} interface.
 * This class is responsible for resolving appropriate {@link QueryHandler}s for a given query type.
 * Handlers are prioritized based on their specificity and an optional {@link HandlerPriority} annotation.
 * The class ensures that handlers matching the exact query type (specificity 0) are preferred,
 * and fall back to less specific handlers when necessary.
 *
 * <p>Key features of this implementation:</p>
 * <ul>
 *   <li>Filters handlers based on whether they match the given query type.</li>
 *   <li>Groups handlers by their specificity to determine which handlers are most suitable.</li>
 *   <li>Sorts handlers by their priority (via the {@code @HandlerPriority} annotation)
 *       and specificity in descending order of importance.</li>
 *   <li>Provides a fallback mechanism to include handlers with higher specificity when no exact matches are found.</li>
 * </ul>
 *
 * <p>This class is annotated with {@link Service} to make it available for dependency injection in a Spring context.</p>
 *
 * @see HandlerResolver
 * @see QueryHandler
 * @see HandlerPriority
 */
@Service
public class DefaultHandlerResolver implements HandlerResolver {

    /**
     * Resolves a list of {@link QueryHandler}s for a given query type.
     *
     * <p>This method filters, prioritizes, and sorts the given collection of handlers based on their relevance to
     * the provided query type.
     * Handlers matching the exact query type (specificity 0) are preferred.
     * If no such handlers are found, handlers with higher specificity are included as a fallback.</p>
     *
     * <p>Handlers are sorted by:</p>
     * <ol>
     *   <li>Specificity (lower specificity is preferred).</li>
     *   <li>Priority (higher priority is preferred, determined by {@link HandlerPriority}).</li>
     * </ol>
     *
     * @param queryType the type of query for which handlers are being resolved.
     * @param handlers a collection of {@link QueryHandler} instances to filter and prioritize.
     * @param <Q> the type of the query.
     * @param <R> the type of the response returned by the handlers.
     * @return a sorted list of {@link QueryHandler}s relevant to the given query type.
     */
    @Override
    public <Q, R> List<QueryHandler<Q, R>> resolveHandlers(Class<?> queryType, Collection<QueryHandler> handlers) {
        // Filter and group handlers by specificity
        var groupedHandlers = handlers.stream()
                .filter(handler -> matchesQueryType(handler, queryType))
                .map(handler -> (QueryHandler<Q, R>) handler)
                .collect(Collectors.groupingBy(handler -> getSpecificity(getHandlerQueryType(handler), queryType)));

        // Prefer handlers with specificity 0, fallback to all other handlers if none found
        return groupedHandlers.getOrDefault(0, groupedHandlers.values().stream()
                        .flatMap(Collection::stream)
                        .toList())
                .stream()
                // Sort based on specificity and priority
                .sorted(Comparator.comparingInt((QueryHandler<Q, R> handler) ->
                                getSpecificity(getHandlerQueryType(handler), queryType))
                        .thenComparingInt(this::getHandlerPriority).reversed())
                .toList();
    }

    /**
     * Determines whether a {@link QueryHandler} matches the given query type.
     *
     * <p>A handler is considered to match if its query type is assignable from the provided query type.</p>
     *
     * @param handler the {@link QueryHandler} to check.
     * @param queryType the query type to match against.
     * @return {@code true} if the handler matches the query type, {@code false} otherwise.
     */
    private boolean matchesQueryType(QueryHandler handler, Class<?> queryType) {
        ParameterizedType parameterizedType = (ParameterizedType) handler
                .getClass()
                .getGenericInterfaces()[0];
        Class<?> handlerQueryType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return handlerQueryType.isAssignableFrom(queryType);
    }

    /**
     * Retrieves the priority of a handler using the {@link HandlerPriority} annotation.
     *
     * <p>If no {@link HandlerPriority} annotation is present on the handler, a default priority of {@code 0} is returned.</p>
     *
     * @param handler the {@link QueryHandler} to inspect for priority.
     * @return the priority value specified in the {@link HandlerPriority} annotation, or {@code 0} if not present.
     */
    private int getHandlerPriority(Object handler) {
        HandlerPriority priorityAnnotation = handler.getClass().getAnnotation(HandlerPriority.class);
        return priorityAnnotation != null ? priorityAnnotation.value() : 0; // Default priority is 0
    }

    /**
     * Retrieves the query type handled by a {@link QueryHandler}.
     *
     * <p>The query type is determined by inspecting the generic type parameters of the handler's interface.</p>
     *
     * @param handler the {@link QueryHandler} whose query type is to be retrieved.
     * @return the {@link Class} representing the query type handled by the handler.
     */
    private Class<?> getHandlerQueryType(Object handler) {
        ParameterizedType parameterizedType = (ParameterizedType) handler
                .getClass()
                .getGenericInterfaces()[0];
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * Calculates the specificity of a handler's query type relative to the given query type.
     *
     * <p>Specificity is determined by counting the number of superclass levels between the handler's query type
     * and the provided query type. A specificity of {@code 0} indicates an exact match.</p>
     *
     * @param handlerQueryType the query type of the handler.
     * @param queryType the query type to compare against.
     * @return the specificity value, where {@code 0} indicates an exact match and higher values indicate greater distance.
     */
    private int getSpecificity(Class<?> handlerQueryType, Class<?> queryType) {
        int specificity = 0;
        Class<?> current = queryType;
        while (current != null && !current.equals(handlerQueryType)) {
            specificity++;
            current = current.getSuperclass();
        }
        return specificity;
    }
}
