package de.tudl.playground.datorum.gateway.query;

import java.util.Collection;
import java.util.List;

public interface HandlerResolver {
    <Q, R> List<QueryHandler<Q, R>> resolveHandlers(Class<?> queryType, Collection<QueryHandler> handlers);
}

