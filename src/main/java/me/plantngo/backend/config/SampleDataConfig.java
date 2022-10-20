package me.plantngo.backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Preference;
import me.plantngo.backend.repositories.CustomerRepository;

@Configuration
public class SampleDataConfig {

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            Preference preference = new Preference();

            Customer cust1 = new Customer(
                    null,
                    "soonann",
                    "soonann@example.com",
                    "Password123!",
                    null,
                    0,
                    null,
                    null,
                    null,
                    null);
            Customer cust2 = new Customer(
                    null,
                    "gabriel",
                    "gabriel@example.com",
                    "Password123!",
                    null,
                    0,
                    null,
                    null,
                    null,
                    null);

            customerRepository.saveAll(List.of(cust1, cust2));

        };
    }
}
