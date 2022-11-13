    package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import me.plantngo.backend.DTO.UpdateCustomerDetailsDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    /**
     * Gets customer with given username
     * 
     * @param username
     * @return
     */
    public Customer getCustomerByUsername(String username) {
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);
        if (optionalCustomer.isEmpty()) {
            throw new UserNotFoundException("Username not found");
        }
        return optionalCustomer.get();
    }
    
    /**
     * Gets customer with given email
     * 
     * @param email
     * @return
     */
    public Customer getCustomerByEmail(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isEmpty()) {
            throw new UserNotFoundException("Email not found");
        }
        return optionalCustomer.get();
    }

    /**
     * Gets all customers
     * 
     * @return
     */
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * Updates existing customer with given username using details from updateCustomerDetailsDTO
     * 
     * @param username
     * @param updateCustomerDetailsDTO
     * @return
     */
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

    /**
     * Deletes customer with given username
     * 
     * @param username
     */
    @Transactional
    public void deleteCustomer(String username) {
        if (!customerRepository.existsByUsername(username)) {
            throw new NotExistException("Customer");
        }
        customerRepository.deleteByUsername(username);
    }


}
