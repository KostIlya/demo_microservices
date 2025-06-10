package ru.t1.demo_t1.model;

import jakarta.persistence.*;
import lombok.*;
import ru.t1.core.model.enums.StatusClientEnum;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "middle_name", nullable = false)
    private String middleName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "client_id", nullable = false, unique = true)
    private UUID clientId;
    @Column(name = "status")
    private StatusClientEnum status;
}
