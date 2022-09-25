package me.plantngo.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.RegistrationDTO;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@Service
public class AuthService {
    
    private CustomerRepository customerRepository;
    private MerchantRepository merchantRepository;

    @Autowired
    public AuthService(CustomerRepository customerRepository, MerchantRepository merchantRepository) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
    }

    public ResponseEntity<String> registerCustomer(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (customerRepository.existsByEmail(registrationDTO.getEmail())) {
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if username is already in use
        if (customerRepository.existsByUsername(registrationDTO.getUsername())) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }
        
        Customer customer = new Customer();
        customer.setEmail(registrationDTO.getEmail());
        customer.setUsername(registrationDTO.getUsername());
        customer.setPassword(registrationDTO.getPassword());
        customer.setGreenPts(0);

        customerRepository.save(customer);

        return new ResponseEntity<>("Customer registered!", HttpStatus.CREATED);
    }

    public ResponseEntity<String> registerMerchant(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (merchantRepository.existsByEmail(registrationDTO.getEmail())) {
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if username is already in use
        if (merchantRepository.existsByUsername(registrationDTO.getUsername())) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }
        
        Merchant merchant = new Merchant();
        merchant.setEmail(registrationDTO.getEmail());
        merchant.setUsername(registrationDTO.getUsername());
        merchant.setPassword(registrationDTO.getPassword());
        merchant.setCompany(registrationDTO.getCompany());

        merchantRepository.save(merchant);

        return new ResponseEntity<>("Merchant registered!", HttpStatus.CREATED);

    }
}
