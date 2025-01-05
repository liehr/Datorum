package de.tudl.playground.datorum.gateway;

import de.tudl.playground.datorum.gateway.command.DefaultCommandGateway;
import org.junit.jupiter.api.BeforeEach;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultCommandGatewayTests {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private DefaultCommandGateway commandGateway;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        commandGateway = new DefaultCommandGateway(eventPublisher);
    }

    @Test
    void testSendCommand() {
        Object command = new Object();

        commandGateway.send(command);

        verify(eventPublisher, times(1)).publishEvent(command);
    }

    @Test
    void testSendWithNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> commandGateway.send(null));
    }

    @Test
    void testSendWithEmptyCommand() {
        Object emptyCommand = new Object();
        commandGateway.send(emptyCommand);

        verify(eventPublisher, times(1)).publishEvent(emptyCommand);
    }

    @Test
    void testSendWithLargeCommand() {
        StringBuilder largePayload = new StringBuilder();
        largePayload.append("data".repeat(1_000_000));
        Object largeCommand = new String(largePayload);

        commandGateway.send(largeCommand);

        verify(eventPublisher, times(1)).publishEvent(largeCommand);
    }

    @Test
    void testSendCommandConcurrently() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                Object command = new Object();
                commandGateway.send(command);
            });
        }

        executorService.shutdown();

        await().atMost(Duration.ofSeconds(5)).until(executorService::isTerminated);

        verify(eventPublisher, times(100)).publishEvent(any(Object.class));
    }

    @Test
    void testSendCommandWithPublisherException() {
        doThrow(new RuntimeException("Publisher Error")).when(eventPublisher).publishEvent(any());

        Object command = new Object();

        assertDoesNotThrow(() -> commandGateway.send(command));
    }

    @Test
    void testSendMultipleCommands() {
        for (int i = 0; i < 10_000; i++) {
            Object command = new Object();
            commandGateway.send(command);
        }

        verify(eventPublisher, times(10_000)).publishEvent(any(Object.class));
    }

    @Test
    void testSendWithCustomCommand() {
        class CustomCommand {
        }

        Object customCommand = new CustomCommand();

        commandGateway.send(customCommand);

        verify(eventPublisher, times(1)).publishEvent(customCommand);
    }


}

