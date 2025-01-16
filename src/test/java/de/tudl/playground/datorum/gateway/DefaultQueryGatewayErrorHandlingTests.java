package de.tudl.playground.datorum.gateway;

import de.tudl.playground.datorum.gateway.helpers.TestHandlers;
import de.tudl.playground.datorum.gateway.helpers.TestQueries;
import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.gateway.query.exception.ErrorHandlingQueryException;
import de.tudl.playground.datorum.gateway.query.resolver.DefaultHandlerResolver;
import de.tudl.playground.datorum.gateway.query.DefaultQueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultQueryGatewayErrorHandlingTests
{
    private ApplicationContext applicationContext;
    private DefaultQueryGateway queryGateway;

    @BeforeEach
    public void setup() {
        applicationContext = mock(ApplicationContext.class);
        DefaultHandlerResolver defaultHandlerResolver = new DefaultHandlerResolver();
        queryGateway = new DefaultQueryGateway(applicationContext, defaultHandlerResolver);
    }

    @Test
    void testHandlerThrowsException()
    {
        Object object = new Object();
        TestHandlers.ThrowingQueryHandler throwingQueryHandler = new TestHandlers.ThrowingQueryHandler();

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("throwingQueryHandler", throwingQueryHandler));

        assertThrows(ErrorHandlingQueryException.class, () -> queryGateway.query(object));
    }

    @Test
    void testQueryWithNullValue()
    {
        assertThrows(IllegalArgumentException.class, () -> queryGateway.query(null));
    }

    @Test
    void testHandlerWithIncorrectTypeArguments() {
        TestQueries.SampleQuery sampleQuery = new TestQueries.SampleQuery();
        TestHandlers.ValidQueryHandler validQueryHandler = new TestHandlers.ValidQueryHandler();
        TestHandlers.InvalidQueryHandler invalidQueryHandler = new TestHandlers.InvalidQueryHandler();

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("validQueryHandler", validQueryHandler, "invalidQueryHandler", invalidQueryHandler));

        Optional<String> result = queryGateway.query(sampleQuery);

        assertTrue(result.isPresent());
        assertEquals("Handled by ValidQueryHandler", result.get());
    }

    @Test
    void testHandlerReturnsNull()
    {
        TestQueries.GenericQuery genericQuery = new TestQueries.GenericQuery();
        TestHandlers.NullQueryHandler nullQueryHandler = new TestHandlers.NullQueryHandler();

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("nullQueryHandler", nullQueryHandler));

        Optional<String> result = queryGateway.query(genericQuery);

        assertNull(result);
    }
}
