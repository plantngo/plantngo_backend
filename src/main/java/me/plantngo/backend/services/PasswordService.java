package me.plantngo.backend.services;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

public class PasswordService {
    ///@PostMapping("/where do i post to")
    public String processRegister(Customer customer, CustomerRepository customerRepository) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        customerRepository.save(customer);

        return "Registration successful";
    }

}
