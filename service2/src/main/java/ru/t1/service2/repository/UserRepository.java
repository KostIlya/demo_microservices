package ru.t1.service2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.service2.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String username);

    Boolean existsByLogin(String username);

}
