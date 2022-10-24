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

    @Autowired
    public ChangeCredentialService(CustomerRepository customerRepository,
                                   MerchantRepository merchantRepository,
                                   BCryptPasswordEncoder bCryptPasswordEncoder,
                                   CustomerService customerService,
                                   MerchantService merchantService){
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void validateNewUsername(String newUsername, Character userType){
        try{
            if(userType == 'C')
                customerRepository.findByUsername(newUsername).get();
            else if(userType == 'M')
                merchantRepository.findByUsername(newUsername).get();
            else
                throw new IllegalArgumentException("Invalid user type");

            throw new AlreadyExistsException("Username already taken");
        } catch (NoSuchElementException e){}
    }

    public ResponseEntity<String> replaceUsername(String oldUsername, String newUsername, Character userType){
        if(userType == 'C') {
            try{
                Customer customer = customerRepository.findByUsername(oldUsername).get();
                return replaceCustomerUsername(customer, newUsername);
            } catch (NoSuchElementException e){
                throw new UserNotFoundException("User does not exist");
            }
        }
        else {
            try{
                Merchant merchant = merchantRepository.findByUsername(oldUsername).get();
                return replaceMerchantUsername(merchant, newUsername);
            } catch (NoSuchElementException e){
                throw new UserNotFoundException("User does not exist");
            }
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
            try{
                Customer customer = customerRepository.findByUsername(username).get();
                return replaceCustomerPassword(customer, newPassword);
            } catch (NoSuchElementException e){
                throw new UserNotFoundException("User does not exist");
            }
        }
        else if(userType == 'M') {
            try{
                Merchant merchant = merchantRepository.findByUsername(username).get();
                return replaceMerchantPassword(merchant, newPassword);
            } catch (NoSuchElementException e){
                throw new UserNotFoundException("User does not exist");
            }
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
