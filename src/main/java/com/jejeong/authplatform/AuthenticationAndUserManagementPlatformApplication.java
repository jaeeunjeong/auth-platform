package com.jejeong.authplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AuthenticationAndUserManagementPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationAndUserManagementPlatformApplication.class, args);
	}

}
