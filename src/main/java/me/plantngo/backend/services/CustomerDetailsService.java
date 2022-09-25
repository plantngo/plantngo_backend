package me.plantngo.backend.services;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.CustomerDetails;
import me.plantngo.backend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = null;
        if (customerRepository.findByEmail(username).isPresent()) {
            customer = customerRepository.findByEmail(username).get();
        }
        if (customer == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomerDetails(customer);
    }

}