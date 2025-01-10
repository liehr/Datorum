package de.tudl.playground.datorum.gateway.query;

import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

@Service
public class DefaultHandlerResolver implements HandlerResolver {
    @Override
    public <Q, R> List<QueryHandler<Q, R>> resolveHandlers(Class<?> queryType, Collection<QueryHandler> handlers) {
        return handlers.stream()
                .filter(handler -> {
                    ParameterizedType parameterizedType = (ParameterizedType) handler
                            .getClass()
                            .getGenericInterfaces()[0];
                    Class<?> handlerQueryType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    return handlerQueryType.isAssignableFrom(queryType);
                })
                .map(handler -> (QueryHandler<Q, R>) handler)
                .toList();
    }
}
