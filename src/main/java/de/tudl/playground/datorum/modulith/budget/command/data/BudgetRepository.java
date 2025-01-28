package de.tudl.playground.datorum.modulith.budget.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID>
{
    Optional<List<Budget>> findBudgetsByUserId(UUID userId);
}
