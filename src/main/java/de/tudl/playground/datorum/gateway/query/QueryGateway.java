package de.tudl.playground.datorum.gateway.query;

import java.util.Optional;

/**
 * The {@code QueryGateway} interface defines a contract for executing queries within the system.
 * It provides a method for querying data by dispatching a query to the appropriate handler.
 * <p>
 * This interface allows for decoupling the execution of queries from their specific handlers.
 * The implementation of this interface should manage finding the appropriate handler for a given query
 * and returning the corresponding result.
 * </p>
 */
public interface QueryGateway {
    /**
     * Executes the given query and returns the result.
     * <p>
     * The implementation of this method should dynamically locate a suitable {@link QueryHandler}
     * based on the type of the provided query and invoke it to obtain the query result.
     * </p>
     *
     * @param query the query object that needs to be processed.
     * @param <Q>   the type of the query being processed.
     * @param <R>   the type of the result returned by the query handler.
     * @return the result of the query processing.
     */
    <Q, R> Optional<R> query(Q query);
}

