package me.plantngo.backend.services;

import java.util.List;

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
        if (customerRepository.findByUsername(username).isEmpty()) {
            throw new UserNotFoundException("Username not found");
        }
        return customerRepository.findByUsername(username).get();
    }
    
    public Customer getCustomerByEmail(String email) {
        if (customerRepository.findByEmail(email).isEmpty()) {
            throw new UserNotFoundException("Email not found");
        }
        return customerRepository.findByEmail(email).get();
    }

//    public void addGreenPoints(String username, Integer amount){
//        if (customerRepository.findByUsername(username).isEmpty()) {
//            throw new UserNotFoundException("Username not found");
//        }
//        Customer customer = customerRepository.findByUsername(username).get();
//        Integer newBalance = customer.getGreenPts() + amount;
//        customer.setGreenPts(newBalance);
//        customerRepository.save(customer);
//    }
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

}
