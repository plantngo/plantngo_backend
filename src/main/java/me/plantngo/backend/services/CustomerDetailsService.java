package me.plantngo.backend.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;

@Service
public class CustomerDetailsService implements UserDetailsService {
    private CustomerRepository customerRepository;
    
    public CustomerDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        
        Optional<Customer> tempCustomer = customerRepository.findByUsername(username);
        if (tempCustomer.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        Customer customer = tempCustomer.get();

        UserDetails user = User.withUsername(customer.getUsername()).password(customer.getPassword()).authorities("USER").build();
        
        return user;
    }
}
