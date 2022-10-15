package me.plantngo.backend;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}