package de.tudl.playground.datorum.gateway;

import de.tudl.playground.datorum.gateway.helpers.TestHandlers;
import de.tudl.playground.datorum.gateway.helpers.TestQueries;
import de.tudl.playground.datorum.gateway.query.DefaultQueryGateway;
import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.gateway.query.resolver.DefaultHandlerResolver;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultQueryGatewayPerformanceTests
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
    void testPerformanceWithLargeNumberOfHandlers()
    {
        int handlerCount = 1_000_000;
        List<QueryHandler<?, ?>> handlers = generateHandlers(handlerCount);

        Map<String, QueryHandler> handlerMap = handlers.stream()
                        .collect(Collectors.toMap(h -> "handler_" + h.hashCode() + h, Function.identity()));

        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(handlerMap);

        TestQueries.SampleQuery sampleQuery = new TestQueries.SampleQuery();

        long startTime = System.nanoTime();
        Optional<String> result = queryGateway.query(sampleQuery);
        long endTime = System.nanoTime();

        assertTrue(result.isPresent());
        assertEquals("Handled by SampleQueryHandler", result.get());
        long executionTimeMs = (endTime - startTime) / 1_000_000;
        System.out.println("Execution time: " + executionTimeMs + " ms for search in " + (handlerCount + 1) + " Query handlers" );
        assertTrue(executionTimeMs < 500, "Query resolution took too long!");
    }

    @SneakyThrows
    @Test
    void testConcurrentQueries() throws InterruptedException {
        int threadCount = 100; // Number of threads
        int iterationsPerThread = 10; // Queries per thread

        // Register multiple handlers
        List<QueryHandler<?, ?>> handlers = List.of(
                new TestHandlers.SampleQueryHandler(),
                new TestHandlers.GenericQueryHandler()
        );

        Map<String, QueryHandler> handlerMap = handlers.stream()
                .collect(Collectors.toMap(h -> "handler_" + h.hashCode(), Function.identity()));
        when(applicationContext.getBeansOfType(QueryHandler.class)).thenReturn(handlerMap);

        // Define the queries
        List<TestQueries.SampleQuery> sampleQueries = new ArrayList<>();
        for (int i = 0; i < threadCount * iterationsPerThread; i++) {
            sampleQueries.add(new TestQueries.SampleQuery());
        }

        // Prepare threads
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Optional<String>>> results = new ArrayList<>();

        for (TestQueries.SampleQuery query : sampleQueries) {
            results.add(executorService.submit(() -> queryGateway.query(query)));
        }

        executorService.shutdown();

        assertTrue(executorService.awaitTermination(30, TimeUnit.SECONDS));

        // Validate results
        for (Future<Optional<String>> future : results) {
            Optional<String> result = future.get(); // Ensure no exceptions occurred
            assertTrue(result.isPresent(), "Query result should be present");
            assertEquals("Handled by SampleQueryHandler", result.get(), "Incorrect handler used");
        }

        System.out.println("All concurrent queries resolved correctly.");
    }


    private List<QueryHandler<?, ?>> generateHandlers(int count) {
        List<QueryHandler<?, ?>> handlers = new ArrayList<>();
        for (int i = 0; i < count - 1; i++) {
            handlers.add(new TestHandlers.GenericQueryHandler()); // Generic handler for BaseQuery
        }

        handlers.add(new TestHandlers.SampleQueryHandler()); // Add a specific handler at the end
        return handlers;
    }
}
