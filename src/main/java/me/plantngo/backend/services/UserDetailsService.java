package me.plantngo.backend.services;

import java.util.Optional;

import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.MerchantRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private CustomerRepository customerRepository;
    private MerchantRepository merchantRepository;
    
    public UserDetailsService(CustomerRepository customerRepository, MerchantRepository merchantRepository) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        
        Optional<Customer> tempCustomer = customerRepository.findByUsername(username);
        Optional<Merchant> tempMerchant = merchantRepository.findByUsername(username);

        Customer customer = null;
        Merchant merchant = null;
        UserDetails user = null;

        if (tempCustomer.isEmpty() && tempMerchant.isEmpty()) {
            throw new UsernameNotFoundException(username);
        } else if (!tempCustomer.isEmpty()){
            customer = tempCustomer.get();
        } else if (!tempMerchant.isEmpty()){
            merchant = tempMerchant.get();
        }

        if (customer != null){
            user = User.withUsername(customer.getUsername()).password(customer.getPassword()).authorities("USER").build();
        } else if (merchant != null){
            user = User.withUsername(merchant.getUsername()).password(merchant.getPassword()).authorities("USER").build();
        }
        
        return user;
    }
}
