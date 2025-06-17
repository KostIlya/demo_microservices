package ru.t1.demo_t1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({
		"ru.t1.demo_aspect_starter.model",  // Сущности из стартера
		"ru.t1.demo_t1.model"               // Сущности основного приложения
})
@EnableJpaRepositories({
		"ru.t1.demo_aspect_starter.repository",  // Репозитории из стартера
		"ru.t1.demo_t1.repository"               // Репозитории основного приложения
})

public class DemoT1Application {

	public static void main(String[] args) {
		SpringApplication.run(DemoT1Application.class, args);
	}

}
