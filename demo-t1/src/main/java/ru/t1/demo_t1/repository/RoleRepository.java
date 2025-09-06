package ru.t1.demo_t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.demo_t1.model.Role;
import ru.t1.demo_t1.model.enums.RoleEnum;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
