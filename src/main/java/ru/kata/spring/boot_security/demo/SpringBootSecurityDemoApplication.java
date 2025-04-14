package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.kata.spring.boot_security.demo.repositories")
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}

}
