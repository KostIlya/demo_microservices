package ru.t1.demo_aspect_starter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_limit_exceed_log")
public class TimeLimitExceedLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "method_signature", nullable = false)
    private String methodSignature;
    @Column(name = "execution_time", nullable = false)
    private Long executionTime;
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
    @Column(name = "limit_time", nullable = false)
    private Long limitTime;
}