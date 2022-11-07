    package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import me.plantngo.backend.DTO.UpdateCustomerDetailsDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.UpdateMerchantDetailsDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@Service
public class CustomerService {
    
    private CustomerRepository customerRepository;

    private MerchantRepository merchantRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, MerchantRepository merchantRepository) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
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

    public Customer updateCustomer(String username, UpdateCustomerDetailsDTO updateCustomerDetailsDTO) {

        // Check if new username is already taken
        if (merchantRepository.existsByUsername(updateCustomerDetailsDTO.getUsername()) || customerRepository.existsByUsername(updateCustomerDetailsDTO.getUsername())) {
            throw new AlreadyExistsException("Username");
        }

        Customer customer = this.getCustomerByUsername(username);
        
        // Updating Customer
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateCustomerDetailsDTO, customer);

        customerRepository.saveAndFlush(customer);

        return customer;
    }

    public void deleteCustomer(String username) {
        if (!customerRepository.existsByUsername(username)) {
            throw new NotExistException("Customer");
        }
        customerRepository.deleteByUsername(username);
    }


}
