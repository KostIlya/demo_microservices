package ru.t1.demo_t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.demo_t1.model.TimeLimitExceedLog;

public interface TimeLimitExceedLogRepository extends JpaRepository<TimeLimitExceedLog, Long> {
}
