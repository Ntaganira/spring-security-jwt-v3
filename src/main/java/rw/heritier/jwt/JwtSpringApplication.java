package rw.heritier.jwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import rw.heritier.jwt.auth.AuthenticationService;
import rw.heritier.jwt.auth.RegisterRequestDTO;

import static rw.heritier.jwt.model.Role.ADMIN;
import static rw.heritier.jwt.model.Role.MANAGER;

@SpringBootApplication
public class JwtSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSpringApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service) {
		return args -> {
			var admin = RegisterRequestDTO.builder()
					.firstName("Admin")
					.lastName("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			// System.out.println("Admin token: " +
			// service.register(admin).getAccessToken());
		};
	}
}
