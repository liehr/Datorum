package de.tudl.playground.datorum.modulith.user.command.data;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link User} entities in the database.
 * <p>
 * This interface extends {@link JpaRepository} to provide basic CRUD operations
 * for the {@code User} entity, with the primary key type of {@link UUID}.
 * </p>
 * <p>
 * It is annotated with {@link Repository} to indicate it is a Spring Data repository,
 * enabling Spring to automatically implement the required methods.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
}
