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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.UpdateCustomerDTO;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.services.CustomerService;

import javax.validation.Valid;

@RestController()
@RequestMapping(path = "api/v1/customer")
@Api(value = "Customer Controller", description = "Operations pertaining to Customer Model")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerController {
    
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "Get a specific Customer given their Username")
    @GetMapping(path="/{username}")
    //@PreAuthorize("authentication.principal.username == #username || hasRole('ADMIN')")
    public Customer getUserByUsername(@PathVariable("username") String username) {
        return customerService.getCustomerByUsername(username);
    }
    
    @ApiOperation(value = "Get all registered Customers")
    @GetMapping
    public List<Customer> getAllUsers() {
        return customerService.findAll();
    }

    @ApiOperation(value = "Edit a registered Customer's fields")
    @PutMapping(path="/{username}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody UpdateCustomerDTO updateCustomerDTO, 
                                                @PathVariable("username") String username) {
        Customer customer = customerService.updateCustomer(username, updateCustomerDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a registered Customer given their Username")
    @DeleteMapping(path="/{username}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("username") String username) {
        customerService.deleteCustomer(username);
        return new ResponseEntity<>("Customer deleted!", HttpStatus.OK);
    }
}

