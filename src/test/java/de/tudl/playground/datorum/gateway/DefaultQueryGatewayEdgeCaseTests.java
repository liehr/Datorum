package de.tudl.playground.datorum.gateway;

import de.tudl.playground.datorum.gateway.helpers.TestHandlers;
import de.tudl.playground.datorum.gateway.helpers.TestQueries;
import de.tudl.playground.datorum.gateway.query.DefaultHandlerResolver;
import de.tudl.playground.datorum.gateway.query.DefaultQueryGateway;
import de.tudl.playground.datorum.gateway.query.HandlerResolver;
import de.tudl.playground.datorum.gateway.query.QueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class DefaultQueryGatewayEdgeCaseTests {
    private ApplicationContext applicationContext;
    private DefaultQueryGateway queryGateway;

    @BeforeEach
    public void setup() {
        applicationContext = mock(ApplicationContext.class);
        DefaultHandlerResolver defaultHandlerResolver = getDefaultHandlerResolver();
        queryGateway = new DefaultQueryGateway(applicationContext, defaultHandlerResolver);
    }

    private static DefaultHandlerResolver getDefaultHandlerResolver() {
        DefaultHandlerResolver defaultHandlerResolver = new DefaultHandlerResolver();
        return defaultHandlerResolver;
    }

    @Test
    void testMultipleHandlersRegistered()
    {
        TestQueries.SampleQuery sampleQuery = new TestQueries.SampleQuery();
        TestHandlers.SampleQueryHandler queryHandler1 = new TestHandlers.SampleQueryHandler();
        TestHandlers.AnotherQueryHandler queryHandler2 = new TestHandlers.AnotherQueryHandler();

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("sampleQueryHandler", queryHandler1, "anotherQueryHandler", queryHandler2));

        Optional<String> result = queryGateway.query(sampleQuery);

        assertEquals("Handled SampleQuery", result.get());


    }
}
