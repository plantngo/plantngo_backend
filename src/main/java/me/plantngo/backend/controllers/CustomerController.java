package me.plantngo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.services.CustomerService;

@RestController()
@RequestMapping(path = "api/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerController {
    
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    

    // @GetMapping(path="{customerId}")
    // public Customer getUserById(@PathVariable("customerId") Integer id){
    //     return this.customerService.getCustomerById(id);
    // }

    @PostMapping(path="/register")
    public ResponseEntity<String> registerCustomer(Customer customer) {
        
        // Check if email is already in use
        if (!customerService.getCustomerByEmail(customer.getEmail()).isEmpty()) {
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if username is already in use
        if (!customerService.getCustomerById(customer.getUsername()).isEmpty()) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User registered!", HttpStatus.OK);
    }
}

