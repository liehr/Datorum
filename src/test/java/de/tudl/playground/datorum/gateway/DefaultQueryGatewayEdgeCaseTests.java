package de.tudl.playground.datorum.gateway;

import de.tudl.playground.datorum.gateway.helpers.TestHandlers;
import de.tudl.playground.datorum.gateway.helpers.TestQueries;
import de.tudl.playground.datorum.gateway.query.resolver.DefaultHandlerResolver;
import de.tudl.playground.datorum.gateway.query.DefaultQueryGateway;
import de.tudl.playground.datorum.gateway.query.QueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultQueryGatewayEdgeCaseTests {
    private ApplicationContext applicationContext;
    private DefaultQueryGateway queryGateway;

    @BeforeEach
    public void setup() {
        applicationContext = mock(ApplicationContext.class);
        DefaultHandlerResolver defaultHandlerResolver = new DefaultHandlerResolver();
        queryGateway = new DefaultQueryGateway(applicationContext, defaultHandlerResolver);
    }

    @Test
    void testMultipleHandlersRegistered()
    {
        TestQueries.SampleQuery sampleQuery = new TestQueries.SampleQuery();
        TestHandlers.SampleQueryHandler queryHandler1 = new TestHandlers.SampleQueryHandler();
        TestHandlers.AnotherQueryHandler queryHandler2 = new TestHandlers.AnotherQueryHandler();

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("sampleQueryHandler", queryHandler1, "anotherQueryHandler", queryHandler2));

        Optional<?> result = queryGateway.query(sampleQuery);

        assertTrue(result.isPresent());
        assertEquals(List.of("Handled by SampleQueryHandler", "Handled by AnotherQueryHandler"), result.get());
    }

    @Test
    void testHandlersWithOverlappingGenericParameters()
    {
        TestQueries.ChildQuery childQuery = new TestQueries.ChildQuery();
        TestHandlers.ParentQueryHandler parentQueryHandler = new TestHandlers.ParentQueryHandler();
        TestHandlers.ChildQueryHandler childQueryHandler = new TestHandlers.ChildQueryHandler();

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("parentQueryHandler", parentQueryHandler, "childQueryHandler", childQueryHandler));

        Optional<?> result = queryGateway.query(childQuery);

        assertTrue(result.isPresent());
        assertEquals("Handled by ChildQueryHandler", result.get());
    }

    @Test
    void testHandlerForGenericQuery_ButSpecificTypPassed()
    {
        TestQueries.SampleQuery sampleQuery = new TestQueries.SampleQuery();
        TestHandlers.GenericQueryHandler genericQueryHandler = new TestHandlers.GenericQueryHandler();

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(Map.of("genericQueryHandler", genericQueryHandler));

        Optional<?> result = queryGateway.query(sampleQuery);

        assertTrue(result.isPresent());
        assertEquals("Handled by GenericQueryHandler", result.get());
    }


}
