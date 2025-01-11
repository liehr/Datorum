package de.tudl.playground.datorum.gateway.query.resolver;

import de.tudl.playground.datorum.gateway.query.QueryHandler;

import java.util.Collection;
import java.util.List;

public interface HandlerResolver {
    <Q, R> List<QueryHandler<Q, R>> resolveHandlers(Class<?> queryType, Collection<QueryHandler> handlers);
}

