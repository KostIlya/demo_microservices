package ru.t1.demo_t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.t1.core.model.enums.StatusClientEnum;
import ru.t1.demo_t1.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "SELECT * FROM Client c WHERE c.client_id = :clientId",
            nativeQuery = true)
    Optional<Client> findByClientId(UUID clientId);
    @Query(value = "SELECT * FROM Client c WHERE c.status = :status LIMIT :limit",
            nativeQuery = true)
    List<Client> findByStatusAndLimit(StatusClientEnum status, Long limit);
}
