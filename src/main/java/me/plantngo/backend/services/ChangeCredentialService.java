package me.plantngo.backend.services;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChangeCredentialService {

    CustomerRepository customerRepository;
    MerchantRepository merchantRepository;

    @Autowired
    public ChangeCredentialService(CustomerRepository customerRepository, MerchantRepository merchantRepository){
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
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
}
