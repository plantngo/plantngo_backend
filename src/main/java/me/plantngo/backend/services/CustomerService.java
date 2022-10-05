package me.plantngo.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;

@Service
public class CustomerService {
    
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerByUsername(String username) {
        if (customerRepository.findByUsername(username).isEmpty()) {
            System.err.println("User doesn't exist");
            return null;
        }
        return customerRepository.findByUsername(username).get();
    }
    
    public Customer getCustomerByEmail(String email) {
        if (customerRepository.findByEmail(email).isEmpty()) {
            System.out.println("Email doesn't exist");
            return null;
        }
        return customerRepository.findByEmail(email).get();
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

}
