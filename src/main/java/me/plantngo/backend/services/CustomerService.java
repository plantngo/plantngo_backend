package me.plantngo.backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.UpdateCustomerDTO;
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

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(String username, UpdateCustomerDTO updateCustomerDTO) {

        // Check if new username is already taken
        if (merchantRepository.existsByUsername(updateCustomerDTO.getUsername()) || customerRepository.existsByUsername(updateCustomerDTO.getUsername())) {
            throw new AlreadyExistsException("Username");
        }

        Customer customer = this.getCustomerByUsername(username);
        
        // Updating Customer
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateCustomerDTO, customer);

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
