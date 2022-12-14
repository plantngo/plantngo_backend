package me.plantngo.backend.services;

import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ChangeCredentialService {

    CustomerRepository customerRepository;
    MerchantRepository merchantRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String USER_NOT_FOUND_ERROR = "User does not exist";

    @Autowired
    public ChangeCredentialService(CustomerRepository customerRepository,
                                   MerchantRepository merchantRepository,
                                   BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void validateNewUsername(String newUsername, Character userType){

        if (!userType.equals('C') && !userType.equals('M')) {
            throw new IllegalArgumentException("Invalid user type");
        } else if ((userType.equals('M') && merchantRepository.existsByUsername(newUsername)) 
                    || userType.equals('C') && customerRepository.existsByUsername(newUsername)) {
            throw new AlreadyExistsException("Username");
        }

    }

    public ResponseEntity<String> replaceUsername(String oldUsername, String newUsername, Character userType){
        if(userType == 'C') {
            Customer customer = customerRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));
            return replaceCustomerUsername(customer, newUsername);
        }
        else {
            Merchant merchant = merchantRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));
            return replaceMerchantUsername(merchant, newUsername);
        }
    }
    public ResponseEntity<String> replaceCustomerUsername(Customer customer, String newUsername){
        customer.setUsername(newUsername);
        customerRepository.saveAndFlush(customer);
        return new ResponseEntity<>("Successfully changed username to " + newUsername, HttpStatus.OK);
    }

    public ResponseEntity<String> replaceMerchantUsername(Merchant merchant, String newUsername){
        merchant.setUsername(newUsername);
        merchantRepository.saveAndFlush(merchant);
        return new ResponseEntity<>("Successfully changed username to " + newUsername, HttpStatus.OK);
    }
    public ResponseEntity<String> replacePassword(String username, String newPassword, Character userType){
        if(userType == 'C') {
            Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));
            return replaceCustomerPassword(customer, newPassword);
        }
        else if(userType == 'M') {
            Merchant merchant = merchantRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));
            return replaceMerchantPassword(merchant, newPassword);
        } else
            throw new IllegalArgumentException("Invalid user type");
    }
    
    public ResponseEntity<String> replaceCustomerPassword(Customer customer, String newPassword){
        customer.setPassword(bCryptPasswordEncoder.encode(newPassword));
        customerRepository.saveAndFlush(customer);
        return new ResponseEntity<>("Successfully changed password" , HttpStatus.OK);
    }

    public ResponseEntity<String> replaceMerchantPassword(Merchant merchant, String newPassword){
        merchant.setPassword(bCryptPasswordEncoder.encode(newPassword));
        merchantRepository.saveAndFlush(merchant);
        return new ResponseEntity<>("Successfully changed password" , HttpStatus.OK);
    }
}
