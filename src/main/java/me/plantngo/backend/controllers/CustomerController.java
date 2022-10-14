package me.plantngo.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.services.CustomerService;

import javax.validation.Valid;

@RestController()
@RequestMapping(path = "api/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerController {
    
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path="/{username}")
    //@PreAuthorize("authentication.principal.username == #username || hasRole('ADMIN')")
    public Customer getUserByUsername(@PathVariable("username") String username) {
        return customerService.getCustomerByUsername(username);
    }

    @PostMapping(path="/{username}")
    public void addGreenPoints(@PathVariable("username") String username, @RequestBody Integer amount){
        customerService.addGreenPoints(username, amount);
//        return username + " amount:" + amount.toString();
    }

    @GetMapping
    public List<Customer> getAllUsers() {
        return customerService.findAll();
    }

}

