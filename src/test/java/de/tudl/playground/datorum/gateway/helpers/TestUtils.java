package de.tudl.playground.datorum.gateway.helpers;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {
    public static ApplicationContext mockApplicationContextWithHandlers(Map<String, QueryHandler> handlers) {
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBeansOfType(QueryHandler.class)).thenReturn(handlers);
        return context;
    }


}
