package ru.t1.service2.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String login;
    @Column(name = "password", nullable = false)
    private String password;
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
