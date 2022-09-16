package me.plantngo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.models.Customer;
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
    

    @GetMapping(path="{customerId}")
    public Customer getUserById(@PathVariable("customerId") Integer id){
        return this.customerService.getCustomerById(id);
    }

  

}

