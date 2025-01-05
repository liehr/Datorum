package de.tudl.playground.datorum.modulith.eventstore;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing events in the event store.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations for {@link EventStore} entities,
 * as well as custom query methods to retrieve events based on their aggregate ID.
 * </p>
 */
@Repository
public interface EventStoreRepository extends JpaRepository<EventStore, Long> {
    /**
     * Retrieves all events associated with a given aggregate ID.
     * <p>
     * This method is used to retrieve the list of events for a specific aggregate,
     * identified by the aggregate ID. This is helpful for event sourcing scenarios
     * where the history of events for an aggregate needs to be loaded.
     * </p>
     *
     * @param aggregateId the unique identifier of the aggregate whose events are to be retrieved.
     * @return a list of {@link EventStore} entities that belong to the specified aggregate ID.
     */
    List<EventStore> findByAggregateId(String aggregateId);
}
