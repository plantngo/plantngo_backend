package me.plantngo.backend.services;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangeCredentialService {

    CustomerRepository customerRepository;
    MerchantRepository merchantRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ChangeCredentialService(CustomerRepository customerRepository, MerchantRepository merchantRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
