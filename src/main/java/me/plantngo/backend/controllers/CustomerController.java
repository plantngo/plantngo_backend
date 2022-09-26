package me.plantngo.backend.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.RegistrationDTO;
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
    

    @GetMapping(path="{username}")
    public Customer getUserByUsername(@PathVariable("username") String username) {
        return customerService.getCustomerByUsername(username);
    }

    @GetMapping
    public List<Customer> getAllUsers() {
        return customerService.findAll();
    }

    // @GetMapping(path="test")
    // public List<Customer> getAllCustomers() {
    //     List<Customer> customerList = new ArrayList<>();
    //     customerList.add(new Customer(1, "Jack", null, "ojh1@gmail.com", "password1", 0));
    //     customerList.add(new Customer(2, "Jane", null, "ojh1@gmail.com", "password1", 0));

    //     return customerList;
    // }

    // @PostMapping(path="register")
    // public ResponseEntity<String> registerCustomer(@RequestBody RegistrationDTO registrationDTO) {
    //     return customerService.registerCustomer(registrationDTO);
    // }
}

