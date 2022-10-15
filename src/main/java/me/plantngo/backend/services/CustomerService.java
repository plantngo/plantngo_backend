package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
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
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);
        if (optionalCustomer.isEmpty()) {
            throw new UserNotFoundException("Username not found");
        }
        return optionalCustomer.get();
    }
    
    public Customer getCustomerByEmail(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isEmpty()) {
            throw new UserNotFoundException("Email not found");
        }
        return optionalCustomer.get();
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

}
