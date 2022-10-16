package me.plantngo.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import me.plantngo.backend.DTO.UpdateCustomerDTO;
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

    @GetMapping(path = "/{username}")
    // @PreAuthorize("authentication.principal.username == #username ||
    // hasRole('ADMIN')")
    public Customer getUserByUsername(@PathVariable("username") String username) {
        return customerService.getCustomerByUsername(username);
    }

    // @PostMapping(path = "/{username}")
    // public void addGreenPoints(@PathVariable("username") String username, @RequestBody Integer amount) {
    //     customerService.addGreenPoints(username, amount);
    //     // return username + " amount:" + amount.toString();
    // }
//    @PostMapping(path="/{username}")
//    public void addGreenPoints(@PathVariable("username") String username, @RequestBody Integer amount){
//        customerService.addGreenPoints(username, amount);
////        return username + " amount:" + amount.toString();
//    }

    @GetMapping
    public List<Customer> getAllUsers() {
        return customerService.findAll();
    }

    @PutMapping(path = "/{username}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("username") String username,
            @RequestBody UpdateCustomerDTO updateCustomerDTO) {
        Customer customer = customerService.updateCustomer(username, updateCustomerDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{username}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("username") String username) {
        customerService.deleteCustomer(username);
        return new ResponseEntity<>("Customer deleted!", HttpStatus.OK);
    }
}
