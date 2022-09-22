package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.RegistrationDTO;
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
            System.out.println("User doesn't exist");
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

    public ResponseEntity<String> registerCustomer(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (getCustomerByEmail(registrationDTO.getEmail()) != null) {
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if username is already in use
        if (getCustomerByUsername(registrationDTO.getUsername()) != null) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }
        
        Customer customer = new Customer();
        customer.setEmail(registrationDTO.getEmail());
        customer.setUsername(registrationDTO.getUsername());
        customer.setPassword(registrationDTO.getPassword());
        customer.setGreenPts(0);

        customerRepository.save(customer);

        return new ResponseEntity<>("User registered!", HttpStatus.OK);
    }

}
