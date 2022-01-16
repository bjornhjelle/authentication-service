package no.bjornhjelle.authentication;

import no.bjornhjelle.authentication.repositories.UserRepository;
import no.bjornhjelle.authentication.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

	private UserService userService;

	public AuthenticationApplication(UserService userService) {
		this.userService = userService;
	}

	@Bean
	public CommandLineRunner runner(UserRepository repository) {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				System.out.println("Users:");
				System.err.println(userService.getAllUsers());
			}

		};
	}
}
