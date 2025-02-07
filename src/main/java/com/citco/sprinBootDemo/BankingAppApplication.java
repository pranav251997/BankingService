package com.citco.sprinBootDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaRepositories
@OpenAPIDefinition(info = @Info(title = "Banking App", description = "Backend RestAPI in Spring Boot", version = "v10", contact = @Contact(name = "Pranav Vispute", email = "pranavvispute1@gmail.com"), license = @License(name = "Spring Boot Java")))
public class BankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
		// http://localhost:9090/swagger-ui/index.html#/
	}

}
