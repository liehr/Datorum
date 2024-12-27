package de.tudl.playground.datorum.gateway.query;

/**
 * The {@code QueryHandler} interface defines a contract for handling specific types of queries in the system.
 * <p>
 * Implementing this interface allows for the processing of a particular query type and returning a corresponding result.
 * The query handler is responsible for the business logic associated with executing the query.
 * </p>
 *
 * @param <Q> the type of the query to be handled.
 * @param <R> the type of the result returned after handling the query.
 */
public interface QueryHandler<Q, R> {

    /**
     * Handles the provided query and returns the result.
     * <p>
     * The implementation of this method should include the logic for processing the query and
     * producing a result based on the query's parameters.
     * </p>
     *
     * @param query the query object to be processed.
     * @return the result of processing the query.
     */
    R handle(Q query);
}
