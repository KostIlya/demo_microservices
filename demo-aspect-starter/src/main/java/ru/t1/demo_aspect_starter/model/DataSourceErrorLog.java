package ru.t1.demo_aspect_starter.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_source_error_log")
public class DataSourceErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "stacktrace", nullable = false, length = 16000)
    private String stacktrace;
    @Column(name = "message", nullable = false, length = 1500)
    private String message;
    @Column(name = "method_signature", nullable = false)
    private String methodSignature;
}
