package de.tudl.playground.datorum.modulith.eventstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class EventPublisher
{
    private final EventStoreRepository eventStoreRepository;

    public EventPublisher(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @SneakyThrows
    public void publishEvent(Object event)
    {
        String aggregateId = extractAggregateId(event);

        EventStore eventStore = new EventStore();
        eventStore.setAggregateId(aggregateId);
        eventStore.setEventType(event.getClass().getSimpleName());
        eventStore.setEventData(new ObjectMapper().writeValueAsString(event));
        eventStore.setCreatedAt(LocalDateTime.now());

        eventStoreRepository.save(eventStore);
    }

    private String extractAggregateId(Object event)
    {
        Method aggregateIdMethod = findAggregateIdMethod(event.getClass());
        try
        {
            return (String) aggregateIdMethod.invoke(event);
        } catch (Exception e)
        {
            throw new RuntimeException("Error invoking @AggregateId method on event: " + event.getClass().getName(), e);
        }
    }

    private Method findAggregateIdMethod(Class<?> eventClass)
    {
        for (Method method : eventClass.getDeclaredMethods())
        {
            if (method.isAnnotationPresent(AggregateId.class))
            {
                if (method.getReturnType() != String.class)
                {
                    throw new IllegalStateException(
                            "@AggregateId method must return a String: " + method.getName() + " in class " + eventClass.getName()
                    );
                }
                return method;
            }
        }
        throw new IllegalArgumentException("No @AggregateId method found in class: " + eventClass.getName());
    }
}

