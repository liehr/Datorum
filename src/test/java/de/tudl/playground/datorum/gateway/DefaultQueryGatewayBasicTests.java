package de.tudl.playground.datorum.gateway;

import de.tudl.playground.datorum.gateway.helpers.TestHandlers;
import de.tudl.playground.datorum.gateway.helpers.TestQueries;
import de.tudl.playground.datorum.gateway.query.resolver.DefaultHandlerResolver;
import de.tudl.playground.datorum.gateway.query.DefaultQueryGateway;
import de.tudl.playground.datorum.gateway.query.QueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultQueryGatewayBasicTests {

    private ApplicationContext applicationContext;
    private DefaultQueryGateway queryGateway;

    @BeforeEach
    public void setup() {
        applicationContext = mock(ApplicationContext.class);
        DefaultHandlerResolver defaultHandlerResolver = new DefaultHandlerResolver();
        queryGateway = new DefaultQueryGateway(applicationContext, defaultHandlerResolver);
    }

    @Test
    void testNoHandlersRegistered() {
        // Arrange
        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> queryGateway.query(new TestQueries.SampleQuery()));
        assertEquals("No handler found for query type: de.tudl.playground.datorum.gateway.helpers.TestQueries$SampleQuery", exception.getMessage());
    }

    @Test
    void testHandlerForSubclassOfQuery() {
        // Arrange
        TestQueries.ChildQuery childQuery = new TestQueries.ChildQuery();
        TestHandlers.ParentQueryHandler parentQueryHandler = new TestHandlers.ParentQueryHandler();
        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("parentQueryHandler", parentQueryHandler));

        // Act
        Optional<String> result = queryGateway.query(childQuery);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Handled by ParentQueryHandler", result.get());
    }

    @Test
    void testQueryTypeNotMatchableToAnyHandler() {
        // Arrange
        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> queryGateway.query(new TestQueries.GenericQuery()));
        assertEquals("No handler found for query type: de.tudl.playground.datorum.gateway.helpers.TestQueries$GenericQuery", exception.getMessage());
    }
}

