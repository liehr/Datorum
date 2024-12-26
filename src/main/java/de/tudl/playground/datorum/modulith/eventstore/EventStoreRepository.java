package de.tudl.playground.datorum.modulith.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventStoreRepository extends JpaRepository<EventStore, Long>
{
    List<EventStore> findByAggregateId(String aggregateId);
}
