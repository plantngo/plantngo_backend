package me.plantngo.backend;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.plantngo.backend.models.Customer;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

		// Test
		System.out.println(new Customer(100, new ArrayList<String>("Sweet", "Sour")));
	}

}
