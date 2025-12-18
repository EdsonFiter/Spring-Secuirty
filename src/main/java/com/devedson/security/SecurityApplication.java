package com.devedson.security;


import com.devedson.security.dto.RegisterRequest;
import com.devedson.security.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.devedson.security.domain.user.Role.ADMIN;
import static com.devedson.security.domain.user.Role.MANAGER;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run (SecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthService authService
	){
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@gmail.com")
					.password("admin123")
					.role(ADMIN)
					.build();

			var manager = RegisterRequest.builder()
					.firstname("Manager")
					.lastname("Manager")
					.email("manager@gmail.com")
					.password("manager123")
					.role(MANAGER)
					.build();
			System.out.println("Admin Token:  "+ authService.register(admin).getAccessToken());
			System.out.println("Manager Token:  "+ authService.register(manager).getAccessToken());
		};
	}

}
